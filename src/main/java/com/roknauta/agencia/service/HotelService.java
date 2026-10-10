package com.roknauta.agencia.service;

import com.roknauta.agencia.domain.Hotel;
import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.dto.HotelDTO;
import com.roknauta.agencia.repository.HotelRepository;
import com.roknauta.agencia.repository.CidadeRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.HotelMapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends CrudServiceBase<Hotel, HotelDTO> {

    private final HotelRepository repository;
    private final CidadeRepository cidadeRepository;
    private final HotelMapper mapper;

    public HotelService(HotelRepository repository, CidadeRepository cidadeRepository, HotelMapper mapper) {
        this.repository = repository;
        this.cidadeRepository = cidadeRepository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Hotel, Long> getRepository() {
        return repository;
    }

    @Override
    protected HotelDTO toDTO(Hotel entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Hotel toEntity(HotelDTO dto) {
        Hotel entity = mapper.toEntity(dto);
        entity.setCidade(cidadeReference(dto));
        return entity;
    }

    @Override
    protected void updateEntity(HotelDTO dto, Hotel entity) {
        mapper.updateEntity(dto, entity);
        entity.setCidade(cidadeReference(dto));
    }

    @Override
    public List<HotelDTO> find(HotelDTO dto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(dto.getNome()));
        Long cidadeId = dto.getCidade() == null ? null : dto.getCidade().getId();
        return repository.buscar(nome, cidadeId).stream().map(this::toDTO).toList();
    }

    private Cidade cidadeReference(HotelDTO dto) {
        if (dto.getCidade() == null || dto.getCidade().getId() == null) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }
        return cidadeRepository.getReferenceById(dto.getCidade().getId());
    }
}
