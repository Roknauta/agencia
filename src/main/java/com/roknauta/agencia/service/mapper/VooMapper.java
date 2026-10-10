package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Voo;
import com.roknauta.agencia.dto.documento.VooDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        CompanhiaAereaMapper.class, AeroportoMapper.class
})
public interface VooMapper {
    VooDTO toDTO(Voo entity);

    Voo toEntity(VooDTO dto);

    void updateEntity(VooDTO dto, @MappingTarget Voo entity);
}
