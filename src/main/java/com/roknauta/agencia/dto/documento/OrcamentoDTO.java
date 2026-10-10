package com.roknauta.agencia.dto.documento;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrcamentoDTO extends DocumentoViagemDTO {
    private LocalDate validadeAte;
    private Integer adultos = 1;
    private Integer criancas = 0;
    private Integer bebes = 0;
    private String pagamento;
    private String tarifa;
    private BigDecimal valorAereo;
    private BigDecimal taxas;
    private BigDecimal servicos;
    private BigDecimal desconto;
}
