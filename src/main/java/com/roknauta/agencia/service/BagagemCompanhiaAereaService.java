package com.roknauta.agencia.service;

import java.util.List;

import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import com.roknauta.agencia.repository.BagagemCompanhiaAereaRepository;
import com.roknauta.agencia.repository.CompanhiaAereaRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.BagagemCompanhiaAereaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BagagemCompanhiaAereaService
        extends CrudServiceBase<BagagemCompanhiaAerea, BagagemCompanhiaAereaDTO> {

    private final BagagemCompanhiaAereaRepository repository;
    private final CompanhiaAereaRepository companhiaRepository;
    private final BagagemCompanhiaAereaMapper mapper;

    public BagagemCompanhiaAereaService(BagagemCompanhiaAereaRepository repository,
                                        CompanhiaAereaRepository companhiaRepository,
                                        BagagemCompanhiaAereaMapper mapper) {
        this.repository = repository;
        this.companhiaRepository = companhiaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public BagagemCompanhiaAereaDTO save(BagagemCompanhiaAereaDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException(
                    "Bagagem de companhia aérea é imutável; crie uma nova versão.");
        }
        if (dto.getDataReferencia() == null) {
            throw new IllegalArgumentException("Data de referência é obrigatória.");
        }
        return super.save(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        throw new UnsupportedOperationException(
                "Versões de bagagem de companhia aérea não podem ser excluídas.");
    }

    @Override
    protected JpaRepository<BagagemCompanhiaAerea, Long> getRepository() {
        return repository;
    }

    @Override
    protected BagagemCompanhiaAereaDTO toDTO(BagagemCompanhiaAerea entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected BagagemCompanhiaAerea toEntity(BagagemCompanhiaAereaDTO dto) {
        BagagemCompanhiaAerea entity = mapper.toEntity(dto);
        setReferences(dto, entity);
        return entity;
    }

    @Override
    protected void updateEntity(BagagemCompanhiaAereaDTO dto, BagagemCompanhiaAerea entity) {
        throw new UnsupportedOperationException(
                "Bagagem de companhia aérea é imutável; crie uma nova versão.");
    }

    @Override
    public List<BagagemCompanhiaAereaDTO> find(BagagemCompanhiaAereaDTO dto) {
        Long companhiaId = dto.getCompanhiaAerea() == null ? null : dto.getCompanhiaAerea().getId();
        return repository.buscar(companhiaId, dto.getTipoBagagem(), dto.getDataReferencia())
                .stream().map(this::toDTO).toList();
    }

    public List<BagagemCompanhiaAereaDTO> findByTexto(String texto) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscarPorTexto(filtro).stream().map(this::toDTO).toList();
    }

    private void setReferences(BagagemCompanhiaAereaDTO dto, BagagemCompanhiaAerea entity) {
        if (dto.getCompanhiaAerea() == null || dto.getCompanhiaAerea().getId() == null
                || dto.getTipoBagagem() == null || dto.getAlturaCm() == null
                || dto.getLarguraCm() == null || dto.getProfundidadeCm() == null
                || dto.getPesoMaximo() == null || dto.getDataReferencia() == null) {
            throw new IllegalArgumentException("Todos os campos da bagagem são obrigatórios.");
        }
        entity.setCompanhiaAerea(companhiaRepository.getReferenceById(dto.getCompanhiaAerea().getId()));
    }
}
