package com.roknauta.agencia.service;

import java.util.List;

import com.roknauta.agencia.domain.documento.Agencia;
import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.repository.AgenciaRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.AgenciaMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgenciaService extends CrudServiceBase<Agencia, AgenciaDTO> {

    private static final String CHAVE_CONFIGURACAO = "PRINCIPAL";

    private final AgenciaRepository repository;
    private final AgenciaMapper mapper;

    public AgenciaService(AgenciaRepository repository, AgenciaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Agencia, Long> getRepository() {
        return repository;
    }

    @Override
    protected AgenciaDTO toDTO(Agencia entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Agencia toEntity(AgenciaDTO dto) {
        Agencia entity = mapper.toEntity(dto);
        entity.setChaveConfiguracao(CHAVE_CONFIGURACAO);
        return entity;
    }

    @Override
    protected void updateEntity(AgenciaDTO dto, Agencia entity) {
        mapper.updateEntity(dto, entity);
        entity.setChaveConfiguracao(CHAVE_CONFIGURACAO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenciaDTO> find(AgenciaDTO filtro) {
        return repository.findFirstByOrderByIdAsc().map(mapper::toDTO).stream().toList();
    }

    @Transactional(readOnly = true)
    public AgenciaDTO configuracao() {
        return repository.findFirstByOrderByIdAsc()
                .map(mapper::toDTO)
                .orElseGet(AgenciaDTO::new);
    }

    @Override
    @Transactional
    public AgenciaDTO save(AgenciaDTO dto) {
        if (dto.getId() == null) {
            repository.findFirstByOrderByIdAsc().ifPresent(agencia -> dto.setId(agencia.getId()));
        }
        return super.save(dto);
    }
}
