package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.dto.HotelDTO;

import com.roknauta.agencia.domain.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CidadeMapper.class)
public interface HotelMapper {

    HotelDTO toDTO(Hotel entity);

    Hotel toEntity(HotelDTO dto);

    void updateEntity(HotelDTO dto, @MappingTarget Hotel entity);
}
