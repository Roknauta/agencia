package com.roknauta.agencia.service;

import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.domain.Pais;
import com.roknauta.agencia.dto.CidadeDTO;
import com.roknauta.agencia.repository.CidadeRepository;
import com.roknauta.agencia.repository.PaisRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.CidadeMapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CidadeService extends CrudServiceBase<Cidade, CidadeDTO> {

    private final CidadeRepository repository;
    private final PaisRepository paisRepository;
    private final CidadeMapper mapper;

    public CidadeService(CidadeRepository repository, PaisRepository paisRepository,
                             CidadeMapper mapper) {
        this.repository = repository;
        this.paisRepository = paisRepository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Cidade, Long> getRepository() {
        return repository;
    }

    @Override
    protected CidadeDTO toDTO(Cidade entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Cidade toEntity(CidadeDTO dto) {
        Cidade entity = mapper.toEntity(dto);
        entity.setPais(paisReference(dto));
        return entity;
    }

    @Override
    protected void updateEntity(CidadeDTO dto, Cidade entity) {
        mapper.updateEntity(dto, entity);
        entity.setPais(paisReference(dto));
    }

    @Override
    public List<CidadeDTO> find(CidadeDTO dto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(dto.getNome()));
        String estado = StringUtils.defaultString(StringUtils.trimToNull(dto.getEstado()));
        Long paisId = dto.getPais() == null ? null : dto.getPais().getId();
        return repository.buscar(nome, estado, paisId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<CidadeDTO> findByTexto(String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscarPorTexto(filtro).stream().map(this::toDTO).toList();
    }

    private Pais paisReference(CidadeDTO dto) {
        return dto.getPais() == null || dto.getPais().getId() == null
                ? null
                : paisRepository.getReferenceById(dto.getPais().getId());
    }
}
