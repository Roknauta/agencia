package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Emissao;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
    uses = {TransferMapper.class, HospedagemMapper.class, AluguelVeiculoMapper.class, SeguroViagemMapper.class,
        ESimMapper.class, ViajanteMapper.class, TrajetoAereoMapper.class, TrajetoRodoviarioMapper.class,
        CidadeMapper.class, BilheteAereoMapper.class, ContratoMapper.class})
public interface EmissaoMapper {

    @Mapping(target = "contrato", source = "contrato", qualifiedByName = "contratoReference")
    EmissaoDTO toDTO(Emissao entity);

    @Mapping(target = "contrato", ignore = true)
    Emissao toEntity(EmissaoDTO dto);

    @Mapping(target = "contrato", ignore = true)
    void updateEntity(EmissaoDTO dto, @MappingTarget Emissao entity);
}
