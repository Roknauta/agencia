package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ServicoDTO extends BaseDTO {

    private BigDecimal valor =  BigDecimal.ZERO;
    private BigDecimal desconto = BigDecimal.ZERO;
    private Moeda moeda = Moeda.BRL;
    private List<TaxaDTO> taxas = new ArrayList<>();
    private String regrasAlteracao;
    private String regrasCancelamento;
    private String observacoes;

    public BigDecimal getValorTotal() {
        BigDecimal totalTaxas = taxas.stream().map(TaxaDTO::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        return valor.add(totalTaxas).subtract(desconto==null?BigDecimal.ZERO:desconto);
    }
}
