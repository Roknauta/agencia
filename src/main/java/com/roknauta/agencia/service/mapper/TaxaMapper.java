package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Taxa;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TipoTaxaMapper.class)
public interface TaxaMapper {

    @Mapping(target = "tipoTaxa", ignore = true)
    Taxa toEntity(TaxaDTO dto);

    TaxaDTO toDTO(Taxa entity);
}
