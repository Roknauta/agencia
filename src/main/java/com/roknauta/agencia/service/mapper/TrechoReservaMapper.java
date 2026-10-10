package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrechoReserva;
import com.roknauta.agencia.dto.documento.TrechoReservaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {VooMapper.class, TarifaAereaMapper.class})
public interface TrechoReservaMapper {
    TrechoReservaDTO toDTO(TrechoReserva entity);

    TrechoReserva toEntity(TrechoReservaDTO dto);

    void updateEntity(TrechoReservaDTO dto, @MappingTarget TrechoReserva entity);
}
