package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.SeguroViagem;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ViajanteMapper.class})
public interface SeguroViagemMapper {

    SeguroViagemDTO toDTO(SeguroViagem entity);

    SeguroViagem toEntity(SeguroViagemDTO dto);

    void updateEntity(SeguroViagemDTO dto, @MappingTarget SeguroViagem entity);
}
