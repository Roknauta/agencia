package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.TarifaAerea;
import com.roknauta.agencia.dto.TarifaAereaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CompanhiaAereaMapper.class, BagagemCompanhiaAereaMapper.class})
public interface TarifaAereaMapper {

    TarifaAereaDTO toDTO(TarifaAerea entity);

    TarifaAerea toEntity(TarifaAereaDTO dto);

    void updateEntity(TarifaAereaDTO dto, @MappingTarget TarifaAerea entity);
}
