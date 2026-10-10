package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Reserva;
import com.roknauta.agencia.dto.documento.ReservaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TrechoReservaMapper.class, BagagemReservaMapper.class})
public interface ReservaMapper {
    ReservaDTO toDTO(Reserva entity);

    Reserva toEntity(ReservaDTO dto);

    void updateEntity(ReservaDTO dto, @MappingTarget Reserva entity);
}
