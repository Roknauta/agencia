package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.Pais;

import com.roknauta.agencia.dto.PaisDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaisMapper {

    PaisDTO toDTO(Pais entity);

    Pais toEntity(PaisDTO dto);

    void updateEntity(PaisDTO dto, @MappingTarget Pais entity);
}
