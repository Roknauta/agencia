package com.roknauta.agencia.service;

import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.domain.TarifaAerea;
import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.repository.BagagemCompanhiaAereaRepository;
import com.roknauta.agencia.repository.CompanhiaAereaRepository;
import com.roknauta.agencia.repository.TarifaAereaRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.TarifaAereaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TarifaAereaService extends CrudServiceBase<TarifaAerea, TarifaAereaDTO> {

    private final TarifaAereaRepository repository;
    private final CompanhiaAereaRepository companhiaRepository;
    private final BagagemCompanhiaAereaRepository bagagemRepository;
    private final TarifaAereaMapper mapper;

    public TarifaAereaService(TarifaAereaRepository repository, CompanhiaAereaRepository companhiaRepository,
        BagagemCompanhiaAereaRepository bagagemRepository, TarifaAereaMapper mapper) {
        this.repository = repository;
        this.companhiaRepository = companhiaRepository;
        this.bagagemRepository = bagagemRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public TarifaAereaDTO save(TarifaAereaDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException("Tarifa aérea é imutável; crie uma nova versão.");
        }
        if (dto.getDataReferencia() == null) {
            throw new IllegalArgumentException("Data de referência é obrigatória.");
        }
        return super.save(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        throw new UnsupportedOperationException("Versões de tarifa aérea não podem ser excluídas.");
    }

    @Override
    protected JpaRepository<TarifaAerea, Long> getRepository() {
        return repository;
    }

    @Override
    protected TarifaAereaDTO toDTO(TarifaAerea entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected TarifaAerea toEntity(TarifaAereaDTO dto) {
        TarifaAerea entity = mapper.toEntity(dto);
        setReferences(dto, entity);
        return entity;
    }

    @Override
    protected void updateEntity(TarifaAereaDTO dto, TarifaAerea entity) {
        throw new UnsupportedOperationException("Tarifa aérea é imutável; crie uma nova versão.");
    }

    @Override
    public List<TarifaAereaDTO> find(TarifaAereaDTO dto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(dto.getNome()));
        Long companhiaId = dto.getCompanhiaAerea() == null ? null : dto.getCompanhiaAerea().getId();
        return repository.buscar(nome, companhiaId, dto.getDataReferencia()).stream().map(this::toDTO).toList();
    }

    public List<TarifaAereaDTO> findByNomeCompanhiaAerea(String nome, Long idCompanhiaAerea) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(nome));
        return repository.buscar(filtro, idCompanhiaAerea, null).stream().map(this::toDTO).toList();
    }

    private void setReferences(TarifaAereaDTO dto, TarifaAerea entity) {
        if (StringUtils.isBlank(dto.getNome()) || dto.getCompanhiaAerea() == null
            || dto.getCompanhiaAerea().getId() == null || StringUtils.isBlank(dto.getRegraCancelamento())
            || StringUtils.isBlank(dto.getRegraAlteracao()) || dto.getDataReferencia() == null) {
            throw new IllegalArgumentException("Nome, companhia aérea, data de referência e regras são obrigatórios.");
        }
        entity.setCompanhiaAerea(companhiaRepository.getReferenceById(dto.getCompanhiaAerea().getId()));
        List<BagagemCompanhiaAerea> bagagens = new ArrayList<>();
        if (dto.getBagagensInclusas() != null) {
            dto.getBagagensInclusas().stream().filter(bagagem -> bagagem != null && bagagem.getId() != null)
                .map(bagagem -> bagagemRepository.getReferenceById(bagagem.getId())).forEach(bagagens::add);
        }
        entity.setBagagensInclusas(bagagens);
    }
}
