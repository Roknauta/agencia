package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.SeguroViagem;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ViajanteMapper.class})
public interface SeguroViagemMapper {

    SeguroViagemDTO toDTO(SeguroViagem entity);

    @Mapping(target = "taxas", ignore = true)
    SeguroViagem toEntity(SeguroViagemDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(SeguroViagemDTO dto, @MappingTarget SeguroViagem entity);
}
