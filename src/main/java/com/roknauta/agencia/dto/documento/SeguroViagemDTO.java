package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeguroViagemDTO extends BaseDTO {

    private String seguradora;
    private String plano;
    private String resumoCobertura;
    private String numeroApolice;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
    private BigDecimal valorTotal;
    private List<ViajanteDTO> viajantesCobertos = new ArrayList<>();
}
