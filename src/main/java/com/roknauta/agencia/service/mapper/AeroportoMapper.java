package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.Aeroporto;
import com.roknauta.agencia.dto.AeroportoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CidadeMapper.class)
public interface AeroportoMapper {

    AeroportoDTO toDTO(Aeroporto entity);

    Aeroporto toEntity(AeroportoDTO dto);

    void updateEntity(AeroportoDTO dto, @MappingTarget Aeroporto entity);
}
