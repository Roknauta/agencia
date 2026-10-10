package com.roknauta.agencia.service.documento;

import java.time.Instant;
import java.util.List;

import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.mapper.OrcamentoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class OrcamentoService {
    private final OrcamentoRepository repository;
    private final OrcamentoMapper mapper;
    private final TrechoReservaService trechoReservaService;

    public OrcamentoService(OrcamentoRepository repository, OrcamentoMapper mapper,
                            TrechoReservaService trechoReservaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.trechoReservaService = trechoReservaService;
    }

    @Transactional(readOnly = true)
    public OrcamentoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> buscar(OrcamentoDTO filtro) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNumero()));
                String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        return repository.buscar(numero, cliente).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public OrcamentoDTO salvar(OrcamentoDTO dto) {
        Orcamento entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
            if (entidade.getStatus() == StatusOrcamento.CONVERTIDO_EM_CONTRATO) {
                throw new IllegalStateException("Orçamento vinculado a contrato não pode ser editado.");
            }
            mapper.updateEntity(dto, entidade);
        }
        trechoReservaService.resolverReferencias(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
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
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }
}
