package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetoAereoDTO extends BaseDTO {
    private SentidoVoo sentido;
    private List<TrechoAereoDTO> trechos = new ArrayList<>();
    private BigDecimal valorTotal;
}
