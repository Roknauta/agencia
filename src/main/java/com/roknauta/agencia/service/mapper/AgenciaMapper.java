package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Agencia;
import com.roknauta.agencia.domain.documento.DadosAgenciaContrato;
import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.dto.documento.DadosAgenciaContratoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AgenciaMapper {
    AgenciaDTO toDTO(Agencia entity);

    Agencia toEntity(AgenciaDTO dto);

    void updateEntity(AgenciaDTO dto, @MappingTarget Agencia entity);

    DadosAgenciaContrato toSnapshot(Agencia entity);

    DadosAgenciaContratoDTO toSnapshotDTO(DadosAgenciaContrato entity);

    DadosAgenciaContrato toSnapshotEntity(DadosAgenciaContratoDTO dto);
}
