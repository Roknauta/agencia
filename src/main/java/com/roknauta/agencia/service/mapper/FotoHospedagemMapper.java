package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.FotoHospedagem;
import com.roknauta.agencia.domain.documento.Hospedagem;
import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.BeanMapping;

@Mapper(componentModel = "spring")
public interface FotoHospedagemMapper {

    FotoHospedagemDTO toDTO(FotoHospedagem entity);

    @Mapping(target = "hospedagem", ignore = true)
    FotoHospedagem toEntity(FotoHospedagemDTO dto);

    @Mapping(target = "hospedagem", ignore = true)
    void updateEntity(FotoHospedagemDTO dto, @MappingTarget FotoHospedagem entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HospedagemDTO toHospedagemReference(Hospedagem hospedagem);
}
