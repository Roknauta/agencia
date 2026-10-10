package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.ESim;
import com.roknauta.agencia.dto.documento.ESimDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ESimMapper {
    ESimDTO toDTO(ESim entity);

    ESim toEntity(ESimDTO dto);

    void updateEntity(ESimDTO dto, @MappingTarget ESim entity);
}
