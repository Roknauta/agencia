package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BagagemReservaDTO extends BaseDTO {
    private BagagemCompanhiaAereaDTO bagagemCompanhiaAerea;
    private int quantidade = 1;
    private String descricao;
}
