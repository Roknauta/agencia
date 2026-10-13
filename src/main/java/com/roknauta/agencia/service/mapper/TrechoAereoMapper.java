package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrechoAereo;
import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VooMapper.class, TarifaAereaMapper.class, BagagemTrechoAereoMapper.class})
public interface TrechoAereoMapper {

    TrechoAereoDTO toDTO(TrechoAereo entity);

    TrechoAereo toEntity(TrechoAereoDTO dto);

    void updateEntity(TrechoAereoDTO dto, @MappingTarget TrechoAereo entity);
}
