package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.BilheteAereo;
import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ViajanteMapper.class, TrechoAereoMapper.class})
public interface BilheteAereoMapper {

    BilheteAereoDTO toDTO(BilheteAereo entity);

    BilheteAereo toEntity(BilheteAereoDTO dto);

    void updateEntity(BilheteAereoDTO dto, @MappingTarget BilheteAereo entity);
}
