package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ReservaMapper.class})
public interface TrajetoAereoMapper {
    TrajetoAereoDTO toDTO(TrajetoAereo entity);

    TrajetoAereo toEntity(TrajetoAereoDTO dto);

    void updateEntity(TrajetoAereoDTO dto, @MappingTarget TrajetoAereo entity);
}
