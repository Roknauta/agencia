package com.roknauta.agencia.service.documento;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.domain.documento.CanalAceiteContrato;
import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.domain.documento.StatusContrato;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.AgenciaService;
import com.roknauta.agencia.service.mapper.ContratoMapper;
import com.roknauta.agencia.web.jasper.JasperUtils;
import com.roknauta.agencia.web.jasper.documento.contrato.ContratoRelatorioDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ContratoService implements CrudService<ContratoDTO> {

    private final ContratoRepository repository;
    private final OrcamentoRepository orcamentoRepository;
    private final ContratoMapper mapper;
    private final TrechoAereoService trechoAereoService;
    private final AgenciaService agenciaService;
    private final ServicoTaxaService servicoTaxaService;

    public ContratoService(ContratoRepository repository, OrcamentoRepository orcamentoRepository,
        ContratoMapper mapper, TrechoAereoService trechoAereoService, AgenciaService agenciaService,
        ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.orcamentoRepository = orcamentoRepository;
        this.mapper = mapper;
        this.trechoAereoService = trechoAereoService;
        this.agenciaService = agenciaService;
        this.servicoTaxaService = servicoTaxaService;
    }

    @Transactional(readOnly = true)
    public ContratoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public ContratoDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public List<ContratoDTO> buscar(ContratoDTO filtro) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNumero()));
        String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        Long orcamentoId = filtro.getOrcamento() == null ? null : filtro.getOrcamento().getId();
        return repository.buscar(numero, cliente, orcamentoId).stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ContratoDTO> buscarDisponiveisParaEmissao(String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscarDisponiveisParaEmissao(filtro, StatusContrato.PAGAMENTO_CONFIRMADO).stream()
            .map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratoDTO> find(ContratoDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratoDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public ContratoDTO save(ContratoDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        throw new UnsupportedOperationException("Contrato é imutável e não pode ser excluído.");
    }

    @Transactional(readOnly = true)
    public byte[] gerarPdf(Long id, Map<String, Object> parametros) throws IOException, JRException {
        ContratoDTO contrato = buscarPorId(id);
        AgenciaDTO agencia = agenciaService.configuracao();
        return JasperUtils.gerarContratoPdf(new ContratoRelatorioDTO(contrato, agencia), parametros);
    }

    @Transactional
    public ContratoDTO salvar(ContratoDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException("Contrato é imutável; crie um novo registro.");
        }
        entidadeOrcamentoObrigatorio(dto);
        Orcamento orcamento = orcamentoRepository.findById(dto.getOrcamento().getId())
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        dto.setMoeda(orcamento.getMoeda());
        if (orcamento.getStatus() != StatusOrcamento.CLIENTE_INTERESSADO) {
            throw new IllegalStateException("O contrato exige um orçamento com interesse do cliente registrado.");
        }
        if (repository.existsByOrcamentoId(orcamento.getId())) {
            throw new IllegalStateException("Este orçamento já está vinculado a um contrato.");
        }
        validarConsultaEPreReserva(dto);
        dto.setNumero(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
            + UUID.randomUUID().toString().substring(0, 8));
        Contrato contrato = mapper.toEntity(dto);
        contrato.setOrcamento(orcamento);
        contrato.setStatus(StatusContrato.AGUARDANDO_ACEITE);
        resolverTaxas(dto, contrato);
        resolverViajantesCobertos(dto, contrato);
        limparIdsDosItensCopiados(contrato);
        trechoAereoService.resolverReferencias(dto, contrato);
        Contrato salvo = repository.save(contrato);
        orcamento.setStatus(StatusOrcamento.CONVERTIDO_EM_CONTRATO);
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(salvo);
    }

    @Transactional
    public ContratoDTO registrarAceite(Long id, CanalAceiteContrato canal, String referenciaAceite) {
        Contrato contrato = buscarEntidade(id);
        if (contrato.getStatus() != StatusContrato.AGUARDANDO_ACEITE) {
            throw new IllegalStateException("O contrato não está aguardando aceite.");
        }
        validarPreReservaVigente(contrato);
        if (canal == null || StringUtils.isBlank(referenciaAceite)) {
            throw new IllegalArgumentException("Canal e referência do aceite são obrigatórios.");
        }
        contrato.setStatus(StatusContrato.ACEITO_AGUARDANDO_PAGAMENTO);
        contrato.setDataAceite(Instant.now());
        contrato.setCanalAceite(canal);
        contrato.setReferenciaAceite(referenciaAceite.trim());
        return mapper.toDTO(repository.save(contrato));
    }

    @Transactional
    public ContratoDTO confirmarPagamento(Long id) {
        Contrato contrato = buscarEntidade(id);
        if (contrato.getStatus() != StatusContrato.ACEITO_AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("O contrato ainda não foi aceito ou já teve o pagamento confirmado.");
        }
        validarPreReservaVigente(contrato);
        contrato.setStatus(StatusContrato.PAGAMENTO_CONFIRMADO);
        contrato.setDataPagamentoConfirmado(Instant.now());
        return mapper.toDTO(repository.save(contrato));
    }

    private Contrato buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
    }

    private void validarConsultaEPreReserva(ContratoDTO dto) {
        if (dto.getDataConsultaPrecos() == null || StringUtils.isBlank(dto.getReferenciaPreReserva())
            || dto.getPreReservaValidaAte() == null) {
            throw new IllegalArgumentException(
                "Data da consulta, referência e validade da pré-reserva são obrigatórias para gerar o contrato.");
        }
        if (!dto.getPreReservaValidaAte().isAfter(Instant.now())) {
            throw new IllegalArgumentException("A pré-reserva precisa estar vigente para gerar o contrato.");
        }
    }

    private void validarPreReservaVigente(Contrato contrato) {
        if (contrato.getPreReservaValidaAte() == null || !contrato.getPreReservaValidaAte().isAfter(Instant.now())) {
            throw new IllegalStateException("O prazo da pré-reserva expirou.");
        }
    }

    private void entidadeOrcamentoObrigatorio(ContratoDTO dto) {
        if (dto.getOrcamento() == null || dto.getOrcamento().getId() == null) {
            throw new IllegalArgumentException("Orçamento aprovado é obrigatório para criar o contrato.");
        }
    }

    private void limparIdsDosItensCopiados(Contrato contrato) {
        contrato.getTransfers().forEach(transfer -> {
            transfer.setId(null);
            transfer.getTaxas().forEach(taxa -> taxa.setId(null));
        });
        contrato.getHospedagens().forEach(hospedagem -> {
            hospedagem.setId(null);
            hospedagem.getTaxas().forEach(taxa -> taxa.setId(null));
            hospedagem.getFotos().forEach(foto -> foto.setId(null));
        });
        contrato.getAlugueis().forEach(aluguel -> {
            aluguel.setId(null);
            aluguel.getTaxas().forEach(taxa -> taxa.setId(null));
        });
        contrato.getSegurosViagem().forEach(seguro -> {
            seguro.setId(null);
            seguro.getTaxas().forEach(taxa -> taxa.setId(null));
            seguro.getViajantesCobertos().forEach(viajante -> viajante.setId(null));
        });
        contrato.getESims().forEach(eSim -> {
            eSim.setId(null);
            eSim.getTaxas().forEach(taxa -> taxa.setId(null));
        });
        contrato.getViajantes().forEach(viajante -> viajante.setId(null));
        contrato.getTrajetosAereos().forEach(trajeto -> {
            trajeto.setId(null);
            trajeto.getTaxas().forEach(taxa -> taxa.setId(null));
            trajeto.getTrechos().forEach(trecho -> {
                trecho.setId(null);
                trecho.getBagagens().forEach(bagagem -> bagagem.setId(null));
            });
        });
        contrato.getTrajetosRodoviarios().forEach(trajeto -> {
            trajeto.setId(null);
            trajeto.getTaxas().forEach(taxa -> taxa.setId(null));
        });
    }

    private void resolverTaxas(ContratoDTO dto, Contrato contrato) {
        if (dto.getMoeda() == null) {
            throw new IllegalArgumentException("Informe a moeda do contrato.");
        }
        servicoTaxaService.validarMoedaOrcamento(dto.getAlugueis(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getESims(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getHospedagens(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getSegurosViagem(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTrajetosAereos(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTrajetosRodoviarios(), dto.getMoeda());
        servicoTaxaService.validarMoedaOrcamento(dto.getTransfers(), dto.getMoeda());
        servicoTaxaService.resolver(dto.getAlugueis(), contrato.getAlugueis());
        servicoTaxaService.resolver(dto.getESims(), contrato.getESims());
        servicoTaxaService.resolver(dto.getHospedagens(), contrato.getHospedagens());
        servicoTaxaService.resolver(dto.getSegurosViagem(), contrato.getSegurosViagem());
        servicoTaxaService.resolver(dto.getTrajetosAereos(), contrato.getTrajetosAereos());
        servicoTaxaService.resolver(dto.getTrajetosRodoviarios(), contrato.getTrajetosRodoviarios());
        servicoTaxaService.resolver(dto.getTransfers(), contrato.getTransfers());
    }

    private void resolverViajantesCobertos(ContratoDTO dto, Contrato contrato) {
        List<SeguroViagemDTO> segurosDTO = dto.getSegurosViagem();
        if (segurosDTO == null) {
            return;
        }
        for (int indiceSeguro = 0; indiceSeguro < contrato.getSegurosViagem().size(); indiceSeguro++) {
            List<Viajante> viajantesDoContrato = segurosDTO.get(indiceSeguro).getViajantesCobertos() == null ? List.of()
                : segurosDTO.get(indiceSeguro).getViajantesCobertos().stream().map(viajanteDTO -> {
                    if (viajanteDTO == null || viajanteDTO.getId() == null) {
                        throw new IllegalArgumentException("Selecione viajantes válidos do contrato para o seguro.");
                    }
                    return localizarViajanteDoContrato(contrato, viajanteDTO);
                }).toList();
            contrato.getSegurosViagem().get(indiceSeguro).setViajantesCobertos(viajantesDoContrato);
        }
    }

    private Viajante localizarViajanteDoContrato(Contrato contrato, ViajanteDTO viajanteDTO) {
        return contrato.getViajantes().stream().filter(viajante -> viajanteDTO.getId().equals(viajante.getId()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException(
                "Os viajantes cobertos pelo seguro devem estar incluídos no contrato."));
    }
}
