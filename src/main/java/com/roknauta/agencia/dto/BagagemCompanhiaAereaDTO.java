package com.roknauta.agencia.dto;

import java.time.LocalDate;

import com.roknauta.agencia.domain.TipoBagagem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BagagemCompanhiaAereaDTO extends BaseDTO {

    private CompanhiaAereaDTO companhiaAerea;
    private TipoBagagem tipoBagagem;
    private LocalDate dataReferencia;
    private Double alturaCm;
    private Double larguraCm;
    private Double profundidadeCm;
    private Double pesoMaximo;
}
