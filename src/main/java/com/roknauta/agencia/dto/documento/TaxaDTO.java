package com.roknauta.agencia.dto.documento;

import java.math.BigDecimal;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxaDTO extends BaseDTO {

    private TipoTaxaDTO tipoTaxa;
    private BigDecimal valor;
    private Moeda moeda = Moeda.BRL;
}
