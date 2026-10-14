package com.roknauta.agencia.service.documento;

import java.io.IOException;
import java.util.Map;

import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.mapper.ViajanteMapper;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.OrcamentoMapper;
import com.roknauta.agencia.web.jasper.JasperUtils;
import com.roknauta.agencia.web.jasper.documento.orcamento.OrcamentoRelatorioDTO;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import com.roknauta.agencia.domain.documento.Orcamento;

@Service
public class OrcamentoService implements CrudService<OrcamentoDTO> {

    private final OrcamentoRepository repository;
    private final ContratoRepository contratoRepository;
    private final OrcamentoMapper mapper;
    private final ViajanteMapper viajanteMapper;
    private final ServicoTaxaService servicoTaxaService;

    public OrcamentoService(OrcamentoRepository repository, ContratoRepository contratoRepository,
        OrcamentoMapper mapper, TrechoAereoService trechoAereoService, ViajanteMapper viajanteMapper,
        ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.contratoRepository = contratoRepository;
        this.mapper = mapper;
        this.viajanteMapper = viajanteMapper;
        this.servicoTaxaService = servicoTaxaService;
    }

    @Transactional(readOnly = true)
    public OrcamentoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }

    @Transactional(readOnly = true)
    public byte[] gerarPdf(Long id, Map<String, Object> parametros) throws IOException, JRException {
        Orcamento entidade =
            repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        entidade.getSegurosViagem().size();
        entidade.getESims().size();
        OrcamentoDTO orcamento = mapper.toDTO(entidade);
        return JasperUtils.gerarOrcamentoPdf(new OrcamentoRelatorioDTO(orcamento), parametros);
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> buscar(OrcamentoDTO filtro) {
        String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        String destino = StringUtils.defaultString(StringUtils.trimToNull(filtro.getDestino()));
        Long origemId = filtro.getOrigem() == null ? null : filtro.getOrigem().getId();
        return repository.buscar(cliente, origemId, destino, filtro.getInicio(), filtro.getFim()).stream()
            .map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> findByTexto(String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscar(filtro, null, "", null, null).stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ViajanteDTO> findViajantes(Long idOrcamento, String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        Orcamento orcamento = repository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        return orcamento.getViajantes().stream()
            .filter(viajante -> filtro.isEmpty()
                || (viajante.getNome() != null && viajante.getNome().toLowerCase().contains(filtro.toLowerCase()))
                || (viajante.getDocumento() != null
                    && viajante.getDocumento().toLowerCase().contains(filtro.toLowerCase())))
            .map(viajanteMapper::toDTO).toList();
    }

    @Transactional
    public OrcamentoDTO salvar(OrcamentoDTO dto) {
        Orcamento entidade;
        if (dto.getId() == null) {
            dto.setNumero("ORC-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + UUID.randomUUID().toString().substring(0, 8));
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
            if (entidade.getStatus() == StatusOrcamento.CONVERTIDO_EM_CONTRATO) {
                throw new IllegalStateException("Orçamento vinculado a contrato não pode ser editado.");
            }
            mapper.updateEntity(dto, entidade);
        }
        resolverTaxas(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    @Override
    @Transactional(readOnly = true)
    public OrcamentoDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrcamentoDTO> find(OrcamentoDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrcamentoDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public OrcamentoDTO save(OrcamentoDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Orçamento não encontrado.");
        }
        if (contratoRepository.existsByOrcamentoId(id)) {
            throw new IllegalStateException("Orçamento associado a contrato não pode ser excluído.");
        }
        repository.deleteById(id);
    }

    @Transactional
    public OrcamentoDTO registrarEnvio(Long id) {
        Orcamento orcamento = buscarEntidade(id);
        if (orcamento.getStatus() != StatusOrcamento.RASCUNHO) {
            throw new IllegalStateException("Somente um orçamento em rascunho pode ser enviado.");
        }
        orcamento.setStatus(StatusOrcamento.ENVIADO);
        orcamento.setDataEnvio(Instant.now());
        return mapper.toDTO(repository.save(orcamento));
    }

    @Transactional
    public OrcamentoDTO registrarInteresseCliente(Long id) {
        Orcamento orcamento = buscarEntidade(id);
        if (orcamento.getStatus() != StatusOrcamento.ENVIADO) {
            throw new IllegalStateException("O interesse só pode ser registrado para um orçamento enviado.");
        }
        orcamento.setStatus(StatusOrcamento.CLIENTE_INTERESSADO);
        orcamento.setDataInteresseCliente(Instant.now());
        return mapper.toDTO(repository.save(orcamento));
    }

    private Orcamento buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }

    private void resolverTaxas(OrcamentoDTO dto, Orcamento entidade) {
        if (dto.getMoeda() == null) {
            throw new IllegalArgumentException("Informe a moeda do orçamento.");
        }
        servicoTaxaService.validarMoedaOrcamento(dto.getAlugueis(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getESims(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getHospedagens(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getSegurosViagem(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTrajetosAereos(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTrajetosRodoviarios(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTransfers(), dto.getMoeda());
        servicoTaxaService.resolver(dto.getAlugueis(), entidade.getAlugueis());
        servicoTaxaService.resolver(dto.getESims(), entidade.getESims());
        servicoTaxaService.resolver(dto.getHospedagens(), entidade.getHospedagens());
        servicoTaxaService.resolver(dto.getSegurosViagem(), entidade.getSegurosViagem());
        servicoTaxaService.resolver(dto.getTrajetosAereos(), entidade.getTrajetosAereos());
        servicoTaxaService.resolver(dto.getTrajetosRodoviarios(), entidade.getTrajetosRodoviarios());
        servicoTaxaService.resolver(dto.getTransfers(), entidade.getTransfers());
    }
}
