package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ViajanteMapper {
    ViajanteDTO toDTO(Viajante entity);

    Viajante toEntity(ViajanteDTO dto);

    void updateEntity(ViajanteDTO dto, @MappingTarget Viajante entity);
}
