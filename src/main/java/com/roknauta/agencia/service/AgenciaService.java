package com.roknauta.agencia.service;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;

import com.roknauta.agencia.domain.documento.Agencia;
import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.repository.AgenciaRepository;
import com.roknauta.agencia.repository.CidadeRepository;
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
    private final CidadeRepository cidadeRepository;

    public AgenciaService(AgenciaRepository repository, AgenciaMapper mapper, CidadeRepository cidadeRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.cidadeRepository = cidadeRepository;
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
        entity.setCidade(cidadeReference(dto));
        return entity;
    }

    @Override
    protected void updateEntity(AgenciaDTO dto, Agencia entity) {
        mapper.updateEntity(dto, entity);
        entity.setChaveConfiguracao(CHAVE_CONFIGURACAO);
        entity.setCidade(cidadeReference(dto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenciaDTO> find(AgenciaDTO filtro) {
        return repository.findFirstByOrderByIdAsc().map(mapper::toDTO).stream().toList();
    }

    @Transactional(readOnly = true)
    public AgenciaDTO configuracao() {
        return repository.findFirstByOrderByIdAsc().map(mapper::toDTO).orElseGet(AgenciaDTO::new);
    }

    @Override
    @Transactional
    public AgenciaDTO save(AgenciaDTO dto) {
        if (dto.getCidade() == null || dto.getCidade().getId() == null) {
            throw new IllegalArgumentException("A cidade da agência é obrigatória.");
        }
        String fusoHorario = dto.getCidade().getFusoHorario();
        if (fusoHorario == null || fusoHorario.isBlank()) {
            throw new IllegalArgumentException("A cidade da agência precisa ter um fuso horário configurado.");
        }
        try {
            ZoneId.of(fusoHorario);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Informe um fuso horário IANA válido para a cidade da agência.", e);
        }
        if (dto.getId() == null) {
            repository.findFirstByOrderByIdAsc().ifPresent(agencia -> dto.setId(agencia.getId()));
        }
        return super.save(dto);
    }

    private Cidade cidadeReference(AgenciaDTO dto) {
        return dto.getCidade() == null || dto.getCidade().getId() == null ? null
            : cidadeRepository.getReferenceById(dto.getCidade().getId());
    }
}
