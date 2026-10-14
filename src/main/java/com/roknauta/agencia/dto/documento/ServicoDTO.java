package com.roknauta.agencia.dto.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ServicoDTO extends BaseDTO {

    private BigDecimal valor;
    private BigDecimal desconto;
    private Moeda moeda = Moeda.BRL;
    private List<TaxaDTO> taxas = new ArrayList<>();
    private String regrasAlteracao;
    private String regrasCancelamento;
    private String observacoes;

    public BigDecimal getValorTotal() {
        if (taxas == null || taxas.stream().filter(Objects::nonNull).anyMatch(taxa -> taxa.getMoeda() != moeda)) {
            return null;
        }
        BigDecimal total = valor == null ? BigDecimal.ZERO : valor;
        total = total.subtract(desconto == null ? BigDecimal.ZERO : desconto);
        return total.add(taxas == null ? BigDecimal.ZERO
            : taxas.stream().filter(Objects::nonNull).map(TaxaDTO::getValor).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
