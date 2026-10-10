package com.roknauta.agencia.service.mapper;

import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;

@Mapper(componentModel = "spring", uses = {TransferMapper.class, HospedagemMapper.class, AluguelVeiculoMapper.class, SeguroViagemMapper.class, ESimMapper.class, ViajanteMapper.class, TrajetoAereoMapper.class, TrajetoRodoviarioMapper.class, CidadeMapper.class, AgenciaMapper.class})
public interface ContratoMapper {
    ContratoDTO toDTO(Contrato entity);

    @Mapping(target = "orcamento", ignore = true)
    @Mapping(target = "dadosAgencia", ignore = true)
    Contrato toEntity(ContratoDTO dto);

    @Mapping(target = "orcamento", ignore = true)
    @Mapping(target = "dadosAgencia", ignore = true)
    void updateEntity(ContratoDTO dto, @MappingTarget Contrato entity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    OrcamentoDTO toOrcamentoReference(Orcamento orcamento);
}
