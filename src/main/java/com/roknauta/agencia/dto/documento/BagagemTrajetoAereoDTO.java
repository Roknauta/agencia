package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.TipoBagagem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BagagemTrajetoAereoDTO {

    private TipoBagagem tipoBagagem;
    private int quantidade = 1;
}
