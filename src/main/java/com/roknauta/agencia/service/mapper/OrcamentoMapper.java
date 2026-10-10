package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TransferMapper.class, HospedagemMapper.class, AluguelVeiculoMapper.class, SeguroViagemMapper.class, ESimMapper.class, ViajanteMapper.class, TrajetoAereoMapper.class, TrajetoRodoviarioMapper.class, CidadeMapper.class})
public interface OrcamentoMapper {
    OrcamentoDTO toDTO(Orcamento entity);

    Orcamento toEntity(OrcamentoDTO dto);

    void updateEntity(OrcamentoDTO dto, @MappingTarget Orcamento entity);
}
