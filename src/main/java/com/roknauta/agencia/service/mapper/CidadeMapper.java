package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.dto.CidadeDTO;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PaisMapper.class)
public interface CidadeMapper {

    CidadeDTO toDTO(Cidade entity);

    Cidade toEntity(CidadeDTO dto);

    void updateEntity(CidadeDTO dto, @MappingTarget Cidade entity);
}
