package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CompanhiaAereaMapper.class)
public interface BagagemCompanhiaAereaMapper {

    BagagemCompanhiaAereaDTO toDTO(BagagemCompanhiaAerea entity);

    BagagemCompanhiaAerea toEntity(BagagemCompanhiaAereaDTO dto);

    void updateEntity(BagagemCompanhiaAereaDTO dto, @MappingTarget BagagemCompanhiaAerea entity);
}
