package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.CompanhiaAerea;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = PaisMapper.class)
public interface CompanhiaAereaMapper {

    CompanhiaAereaDTO toDTO(CompanhiaAerea entity);

    CompanhiaAerea toEntity(CompanhiaAereaDTO dto);

    void updateEntity(CompanhiaAereaDTO dto, @MappingTarget CompanhiaAerea entity);
}
