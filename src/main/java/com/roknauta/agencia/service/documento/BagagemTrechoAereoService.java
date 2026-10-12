package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.BagagemTrechoAereo;
import com.roknauta.agencia.dto.documento.BagagemTrechoAereoDTO;
import com.roknauta.agencia.repository.documento.BagagemTrechoAereoRepository;
import com.roknauta.agencia.service.mapper.BagagemTrechoAereoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BagagemTrechoAereoService {
    private final BagagemTrechoAereoRepository repository;
    private final BagagemTrechoAereoMapper mapper;

    public BagagemTrechoAereoService(BagagemTrechoAereoRepository repository, BagagemTrechoAereoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public BagagemTrechoAereoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Bagagem do trecho aéreo não encontrada."));
    }

    @Transactional(readOnly = true)
    public List<BagagemTrechoAereoDTO> buscar(BagagemTrechoAereoDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getDescricao()));
        Long bagagemId = filtro.getBagagemCompanhiaAerea() == null
                ? null : filtro.getBagagemCompanhiaAerea().getId();
        return repository.buscar(texto, bagagemId).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public BagagemTrechoAereoDTO salvar(BagagemTrechoAereoDTO dto) {
        BagagemTrechoAereo entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Bagagem do trecho aéreo não encontrada."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
