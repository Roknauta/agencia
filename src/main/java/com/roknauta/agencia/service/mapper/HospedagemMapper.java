package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Hospedagem;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring", uses = {HotelMapper.class, FotoHospedagemMapper.class})
public interface HospedagemMapper {

    HospedagemDTO toDTO(Hospedagem entity);

    @Mapping(target = "taxas", ignore = true)
    Hospedagem toEntity(HospedagemDTO dto);

    @Mapping(target = "taxas", ignore = true)
    void updateEntity(HospedagemDTO dto, @MappingTarget Hospedagem entity);

    @AfterMapping
    default void vincularFotos(@MappingTarget Hospedagem hospedagem) {
        if (hospedagem.getFotos() != null) {
            hospedagem.getFotos().forEach(foto -> foto.setHospedagem(hospedagem));
        }
    }
}
