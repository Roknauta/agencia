package com.roknauta.agencia.service.documento;

import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.OrcamentoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class OrcamentoService implements CrudService<OrcamentoDTO> {
    private final OrcamentoRepository repository;
    private final ContratoRepository contratoRepository;
    private final OrcamentoMapper mapper;

    public OrcamentoService(OrcamentoRepository repository, ContratoRepository contratoRepository,
                            OrcamentoMapper mapper, TrechoAereoService trechoAereoService) {
        this.repository = repository;
        this.contratoRepository = contratoRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public OrcamentoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> buscar(OrcamentoDTO filtro) {
        String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        String destino = StringUtils.defaultString(StringUtils.trimToNull(filtro.getDestino()));
        Long origemId = filtro.getOrigem() == null ? null : filtro.getOrigem().getId();
        return repository.buscar(cliente, origemId, destino, filtro.getInicio(), filtro.getFim())
                .stream().map(mapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<OrcamentoDTO> findByTexto(String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscar(filtro, null, "", null, null).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public OrcamentoDTO salvar(OrcamentoDTO dto) {
        Orcamento entidade;
        if (dto.getId() == null) {
            dto.setNumero("ORC-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
                    .format(LocalDateTime.now()) + "-" + UUID.randomUUID().toString().substring(0, 8));
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
            if (entidade.getStatus() == StatusOrcamento.CONVERTIDO_EM_CONTRATO) {
                throw new IllegalStateException("Orçamento vinculado a contrato não pode ser editado.");
            }
            mapper.updateEntity(dto, entidade);
        }
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
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orcamento não encontrado."));
    }
}
