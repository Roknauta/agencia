package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrajetoRodoviario;
import com.roknauta.agencia.dto.documento.TrajetoRodoviarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CidadeMapper.class})
public interface TrajetoRodoviarioMapper {

    TrajetoRodoviarioDTO toDTO(TrajetoRodoviario entity);

    @Mapping(target = "taxas", ignore = true)
    TrajetoRodoviario toEntity(TrajetoRodoviarioDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(TrajetoRodoviarioDTO dto, @MappingTarget TrajetoRodoviario entity);
}
