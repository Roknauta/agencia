package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Transfer;
import com.roknauta.agencia.dto.documento.TransferDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    TransferDTO toDTO(Transfer entity);

    Transfer toEntity(TransferDTO dto);

    void updateEntity(TransferDTO dto, @MappingTarget Transfer entity);
}
