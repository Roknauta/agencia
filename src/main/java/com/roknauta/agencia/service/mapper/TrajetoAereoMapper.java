package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TrechoAereoMapper.class})
public interface TrajetoAereoMapper {

    TrajetoAereoDTO toDTO(TrajetoAereo entity);

    @Mapping(target = "taxas", ignore = true)
    TrajetoAereo toEntity(TrajetoAereoDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(TrajetoAereoDTO dto, @MappingTarget TrajetoAereo entity);
}
