package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.ESim;
import com.roknauta.agencia.dto.documento.ESimDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TaxaMapper.class)
public interface ESimMapper {

    ESimDTO toDTO(ESim entity);

    @Mapping(target = "taxas", ignore = true)
    ESim toEntity(ESimDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(ESimDTO dto, @MappingTarget ESim entity);
}
