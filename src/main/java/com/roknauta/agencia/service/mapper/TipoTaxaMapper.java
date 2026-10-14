package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.TipoTaxa;
import com.roknauta.agencia.dto.documento.TipoTaxaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TipoTaxaMapper {

    TipoTaxaDTO toDTO(TipoTaxa entity);

    TipoTaxa toEntity(TipoTaxaDTO dto);

    void updateEntity(TipoTaxaDTO dto, @MappingTarget TipoTaxa entity);
}
