package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TrajetoRodoviario;
import com.roknauta.agencia.dto.documento.TrajetoRodoviarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CidadeMapper.class})
public interface TrajetoRodoviarioMapper {
    TrajetoRodoviarioDTO toDTO(TrajetoRodoviario entity);

    TrajetoRodoviario toEntity(TrajetoRodoviarioDTO dto);

    void updateEntity(TrajetoRodoviarioDTO dto, @MappingTarget TrajetoRodoviario entity);
}
