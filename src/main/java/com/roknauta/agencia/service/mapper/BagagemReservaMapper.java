package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.BagagemReserva;
import com.roknauta.agencia.dto.documento.BagagemReservaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = BagagemCompanhiaAereaMapper.class)
public interface BagagemReservaMapper {
    BagagemReservaDTO toDTO(BagagemReserva entity);

    BagagemReserva toEntity(BagagemReservaDTO dto);

    void updateEntity(BagagemReservaDTO dto, @MappingTarget BagagemReserva entity);
}
