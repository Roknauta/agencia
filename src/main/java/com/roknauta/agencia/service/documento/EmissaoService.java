package com.roknauta.agencia.service.documento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.roknauta.agencia.domain.documento.BilheteAereo;
import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.Emissao;
import com.roknauta.agencia.domain.documento.StatusContrato;
import com.roknauta.agencia.domain.documento.TrechoReserva;
import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.dto.documento.TrechoReservaDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.repository.documento.EmissaoRepository;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.service.mapper.EmissaoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class EmissaoService {
    private final EmissaoRepository repository;
    private final ContratoRepository contratoRepository;
    private final EmissaoMapper mapper;
    private final TrechoReservaService trechoReservaService;

    public EmissaoService(EmissaoRepository repository, ContratoRepository contratoRepository, EmissaoMapper mapper,
                          TrechoReservaService trechoReservaService) {
        this.repository = repository;
        this.contratoRepository = contratoRepository;
        this.mapper = mapper;
        this.trechoReservaService = trechoReservaService;
    }

    @Transactional(readOnly = true)
    public EmissaoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Emissao não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<EmissaoDTO> buscar(EmissaoDTO filtro) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNumero()));
                String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        return repository.buscar(numero, cliente).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public EmissaoDTO salvar(EmissaoDTO dto) {
        Emissao entidade;
        if (dto.getId() == null) {
            Contrato contrato = contratoObrigatorio(dto);
            if (contrato.getStatus() != StatusContrato.PAGAMENTO_CONFIRMADO) {
                throw new IllegalStateException("A emissão exige contrato aceito e com pagamento confirmado.");
            }
            entidade = mapper.toEntity(dto);
            entidade.setContrato(contrato);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Emissao não encontrado."));
            Contrato contrato = contratoObrigatorio(dto);
            if (!Objects.equals(entidade.getContrato().getId(), contrato.getId())) {
                throw new IllegalArgumentException("O contrato vinculado à emissão não pode ser alterado.");
            }
            mapper.updateEntity(dto, entidade);
        }
        trechoReservaService.resolverReferencias(dto, entidade);
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
            bilhete.setTrechoReserva(localizarTrecho(emissao, bilheteDTO.getTrechoReserva()));
        }
    }

    private Viajante localizarViajante(Emissao emissao, ViajanteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("O viajante do bilhete aéreo é obrigatório.");
        }
        return emissao.getViajantes().stream()
                .filter(viajante -> dto.getId() != null
                        ? Objects.equals(viajante.getId(), dto.getId())
                        : Objects.equals(viajante.getDocumento(), dto.getDocumento())
                                && Objects.equals(viajante.getNome(), dto.getNome()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Viajante do bilhete não pertence à emissão."));
    }

    private TrechoReserva localizarTrecho(Emissao emissao, TrechoReservaDTO dto) {
        if (dto == null || dto.getVoo() == null || dto.getVoo().getId() == null) {
            throw new IllegalArgumentException("O trecho do bilhete aéreo é obrigatório.");
        }
        Stream<TrechoReserva> trechos = emissao.getTrajetosAereos().stream()
                .filter(Objects::nonNull)
                .flatMap(trajeto -> trajeto.getReservas().stream())
                .filter(Objects::nonNull)
                .flatMap(reserva -> reserva.getTrechos().stream())
                .filter(Objects::nonNull);
        return trechos.filter(trecho -> Objects.equals(trecho.getVoo().getId(), dto.getVoo().getId())
                        && Objects.equals(trecho.getDataPartida(), dto.getDataPartida())
                        && Objects.equals(trecho.getDataChegada(), dto.getDataChegada()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Trecho do bilhete não pertence à emissão."));
    }
}
