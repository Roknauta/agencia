package com.roknauta.agencia.service;

import com.roknauta.agencia.domain.Pais;
import java.util.List;

import com.roknauta.agencia.dto.PaisDTO;
import com.roknauta.agencia.repository.PaisRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.PaisMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public class PaisService extends CrudServiceBase<Pais, PaisDTO> {

    private final PaisRepository repository;
    private final PaisMapper mapper;

    public PaisService(PaisRepository repository, PaisMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Pais, Long> getRepository() {
        return repository;
    }

    @Override
    protected PaisDTO toDTO(Pais entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Pais toEntity(PaisDTO dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(PaisDTO dto, Pais entity) {
        mapper.updateEntity(dto, entity);
    }

    @Override
    public List<PaisDTO> find(PaisDTO dto) {
        String nome = StringUtils.isNotBlank(dto.getNome()) ? dto.getNome().trim() : "";
        String sigla = StringUtils.isNotBlank(dto.getSigla()) ? dto.getSigla().trim() : "";
        return repository.findByNomeContainingIgnoreCaseAndSiglaContainingIgnoreCaseOrderByNomeAsc(nome, sigla)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<PaisDTO> findByNome(String nome) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(nome));
        return repository.findByNome(filtro)
                .stream()
                .map(this::toDTO)
                .toList();
    }
}
