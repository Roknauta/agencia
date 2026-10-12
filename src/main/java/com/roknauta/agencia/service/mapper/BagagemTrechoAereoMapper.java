package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.BagagemTrechoAereo;
import com.roknauta.agencia.dto.documento.BagagemTrechoAereoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = BagagemCompanhiaAereaMapper.class)
public interface BagagemTrechoAereoMapper {
    BagagemTrechoAereoDTO toDTO(BagagemTrechoAereo entity);

    BagagemTrechoAereo toEntity(BagagemTrechoAereoDTO dto);

    void updateEntity(BagagemTrechoAereoDTO dto, @MappingTarget BagagemTrechoAereo entity);
}
