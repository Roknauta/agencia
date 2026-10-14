package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.AluguelVeiculo;
import com.roknauta.agencia.dto.documento.AluguelVeiculoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = TaxaMapper.class)
public interface AluguelVeiculoMapper {

    AluguelVeiculoDTO toDTO(AluguelVeiculo entity);

    @Mapping(target = "taxas", ignore = true)
    AluguelVeiculo toEntity(AluguelVeiculoDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(AluguelVeiculoDTO dto, @MappingTarget AluguelVeiculo entity);
}
