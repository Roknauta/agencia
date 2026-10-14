package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Transfer;
import com.roknauta.agencia.dto.documento.TransferDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TaxaMapper.class)
public interface TransferMapper {

    TransferDTO toDTO(Transfer entity);

    @Mapping(target = "taxas", ignore = true)
    Transfer toEntity(TransferDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(TransferDTO dto, @MappingTarget Transfer entity);
}
