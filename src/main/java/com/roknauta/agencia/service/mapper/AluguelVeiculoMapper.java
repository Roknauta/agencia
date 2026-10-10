package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.AluguelVeiculo;
import com.roknauta.agencia.dto.documento.AluguelVeiculoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AluguelVeiculoMapper {
    AluguelVeiculoDTO toDTO(AluguelVeiculo entity);

    AluguelVeiculo toEntity(AluguelVeiculoDTO dto);

    void updateEntity(AluguelVeiculoDTO dto, @MappingTarget AluguelVeiculo entity);
}
