package com.roknauta.agencia.service;

import com.roknauta.agencia.domain.CompanhiaAerea;
import com.roknauta.agencia.domain.Pais;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.repository.CompanhiaAereaRepository;
import com.roknauta.agencia.repository.PaisRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.CompanhiaAereaMapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanhiaAereaService extends CrudServiceBase<CompanhiaAerea, CompanhiaAereaDTO> {

    private final CompanhiaAereaRepository repository;
    private final PaisRepository paisRepository;
    private final CompanhiaAereaMapper mapper;

    public CompanhiaAereaService(CompanhiaAereaRepository repository, PaisRepository paisRepository,
                                 CompanhiaAereaMapper mapper) {
        this.repository = repository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<CompanhiaAerea, Long> getRepository() {
        return repository;
    }

    @Override
    protected CompanhiaAereaDTO toDTO(CompanhiaAerea entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected CompanhiaAerea toEntity(CompanhiaAereaDTO dto) {
        CompanhiaAerea entity = mapper.toEntity(dto);
        entity.setPais(paisReference(dto));
        return entity;
    }

    @Override
    protected void updateEntity(CompanhiaAereaDTO dto, CompanhiaAerea entity) {
        mapper.updateEntity(dto, entity);
        entity.setPais(paisReference(dto));
    }

    @Override
    public List<CompanhiaAereaDTO> find(CompanhiaAereaDTO dto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(dto.getNome()));
        Long paisId = dto.getPais() == null ? null : dto.getPais().getId();
        return repository.buscar(nome, paisId).stream().map(this::toDTO).toList();
    }

    public List<CompanhiaAereaDTO> findByNome(String nome) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(nome));
        return repository.buscar(filtro, null).stream().map(this::toDTO).toList();
    }

    private Pais paisReference(CompanhiaAereaDTO dto) {
        if (dto.getPais() == null || dto.getPais().getId() == null) {
            throw new IllegalArgumentException("País é obrigatório.");
        }
        return paisRepository.getReferenceById(dto.getPais().getId());
    }
}
