package com.roknauta.agencia.service.documento;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import com.roknauta.agencia.domain.documento.BilheteAereo;
import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.Emissao;
import com.roknauta.agencia.domain.documento.StatusContrato;
import com.roknauta.agencia.domain.documento.TrechoAereo;
import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.repository.documento.EmissaoRepository;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.service.mapper.EmissaoMapper;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.ContratoMapper;
import com.roknauta.agencia.web.jasper.JasperUtils;
import com.roknauta.agencia.web.jasper.documento.emissao.EmissaoRelatorioDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class EmissaoService implements CrudService<EmissaoDTO> {

    private final EmissaoRepository repository;
    private final ContratoRepository contratoRepository;
    private final EmissaoMapper mapper;
    private final ContratoMapper contratoMapper;
    private final TrechoAereoService trechoAereoService;

    public EmissaoService(EmissaoRepository repository, ContratoRepository contratoRepository, EmissaoMapper mapper,
        ContratoMapper contratoMapper, TrechoAereoService trechoAereoService) {
        this.repository = repository;
        this.contratoRepository = contratoRepository;
        this.mapper = mapper;
        this.contratoMapper = contratoMapper;
        this.trechoAereoService = trechoAereoService;
    }

    @Transactional(readOnly = true)
    public EmissaoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Emissao não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public EmissaoDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public List<EmissaoDTO> buscar(EmissaoDTO filtro) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNumero()));
        String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        return repository.buscar(numero, cliente).stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmissaoDTO> find(EmissaoDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmissaoDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public EmissaoDTO save(EmissaoDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Emissão não encontrada.");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public EmissaoDTO prepararPorContrato(Long contratoId) {
        Contrato contrato = contratoRepository.findById(contratoId)
            .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
        validarContratoElegivel(contrato);
        EmissaoDTO emissao = mapper.fromContrato(contratoMapper.toDTO(contrato));
        emissao.setContrato(contratoMapper.toContratoReference(contrato));
        emissao.setDataDocumento(LocalDate.now());
        return emissao;
    }

    @Transactional(readOnly = true)
    public byte[] gerarPdf(Long id) throws IOException, JRException {
        return JasperUtils.gerarEmissaoPdf(new EmissaoRelatorioDTO(buscarPorId(id)), Map.of());
    }

    @Transactional
    public EmissaoDTO salvar(EmissaoDTO dto) {
        Emissao entidade;
        if (dto.getId() == null) {
            Contrato contrato = contratoObrigatorio(dto);
            validarContratoElegivel(contrato);
            dto.setNumero("EMI-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
                + UUID.randomUUID().toString().substring(0, 8));
            if (dto.getDataDocumento() == null) {
                dto.setDataDocumento(LocalDate.now());
            }
            entidade = mapper.toEntity(dto);
            entidade.setContrato(contrato);
            limparIdsDosItensCopiados(entidade);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Emissao não encontrado."));
            Contrato contrato = contratoObrigatorio(dto);
            if (!Objects.equals(entidade.getContrato().getId(), contrato.getId())) {
                throw new IllegalArgumentException("O contrato vinculado à emissão não pode ser alterado.");
            }
            mapper.updateEntity(dto, entidade);
        }
        trechoAereoService.resolverReferencias(dto, entidade);
        resolverBilhetesAereos(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    private Contrato contratoObrigatorio(EmissaoDTO dto) {
        if (dto.getContrato() == null || dto.getContrato().getId() == null) {
            throw new IllegalArgumentException("Contrato é obrigatório para criar uma emissão.");
        }
        return contratoRepository.findById(dto.getContrato().getId())
            .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
    }

    private void validarContratoElegivel(Contrato contrato) {
        if (contrato.getStatus() != StatusContrato.PAGAMENTO_CONFIRMADO) {
            throw new IllegalStateException("A emissão exige contrato aceito e com pagamento confirmado.");
        }
        if (repository.existsByContratoId(contrato.getId())) {
            throw new IllegalStateException("Este contrato já possui uma emissão.");
        }
    }

    private void resolverBilhetesAereos(EmissaoDTO dto, Emissao emissao) {
        if (dto.getBilhetesAereos() == null || dto.getBilhetesAereos().isEmpty()) {
            emissao.setBilhetesAereos(new ArrayList<>());
            return;
        }
        if (emissao.getBilhetesAereos() == null
            || emissao.getBilhetesAereos().size() != dto.getBilhetesAereos().size()) {
            throw new IllegalArgumentException("A lista de bilhetes aéreos está inconsistente.");
        }
        for (int i = 0; i < dto.getBilhetesAereos().size(); i++) {
            BilheteAereoDTO bilheteDTO = dto.getBilhetesAereos().get(i);
            BilheteAereo bilhete = emissao.getBilhetesAereos().get(i);
            bilhete.setViajante(localizarViajante(emissao, bilheteDTO.getViajante()));
            bilhete.setTrechoAereo(localizarTrecho(emissao, bilheteDTO.getTrechoAereo()));
        }
    }

    private Viajante localizarViajante(Emissao emissao, ViajanteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("O viajante do bilhete aéreo é obrigatório.");
        }
        return emissao.getViajantes().stream()
            .filter(viajante -> dto.getId() != null && Objects.equals(viajante.getId(), dto.getId())
                || Objects.equals(viajante.getDocumento(), dto.getDocumento())
                    && Objects.equals(viajante.getNome(), dto.getNome()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Viajante do bilhete não pertence à emissão."));
    }

    private TrechoAereo localizarTrecho(Emissao emissao, TrechoAereoDTO dto) {
        if (dto == null || dto.getVoo() == null || dto.getVoo().getId() == null) {
            throw new IllegalArgumentException("O trecho do bilhete aéreo é obrigatório.");
        }
        Stream<TrechoAereo> trechos = emissao.getTrajetosAereos().stream().filter(Objects::nonNull)
            .flatMap(trajeto -> trajeto.getTrechos().stream()).filter(Objects::nonNull);
        return trechos
            .filter(trecho -> Objects.equals(trecho.getVoo().getId(), dto.getVoo().getId())
                && Objects.equals(trecho.getDataPartida(), dto.getDataPartida())
                && Objects.equals(trecho.getDataChegada(), dto.getDataChegada()))
            .findFirst().orElseThrow(() -> new IllegalArgumentException("Trecho do bilhete não pertence à emissão."));
    }

    private void limparIdsDosItensCopiados(Emissao emissao) {
        emissao.getTransfers().forEach(transfer -> transfer.setId(null));
        emissao.getHospedagens().forEach(hospedagem -> {
            hospedagem.setId(null);
            hospedagem.getFotos().forEach(foto -> foto.setId(null));
        });
        emissao.getAlugueis().forEach(aluguel -> aluguel.setId(null));
        emissao.getSegurosViagem().forEach(seguro -> {
            seguro.setId(null);
            seguro.getViajantesCobertos().forEach(viajante -> viajante.setId(null));
        });
        emissao.getESims().forEach(eSim -> eSim.setId(null));
        emissao.getViajantes().forEach(viajante -> viajante.setId(null));
        emissao.getTrajetosAereos().forEach(trajeto -> {
            trajeto.setId(null);
            trajeto.getTrechos().forEach(trecho -> {
                trecho.setId(null);
                trecho.getBagagens().forEach(bagagem -> bagagem.setId(null));
            });
        });
        emissao.getTrajetosRodoviarios().forEach(trajeto -> trajeto.setId(null));
        emissao.getBilhetesAereos().forEach(bilhete -> bilhete.setId(null));
    }
}
