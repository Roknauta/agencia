package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.SentidoVoo;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetoAereoDTO extends ServicoDTO {

    private SentidoVoo sentido;
    private List<TrechoAereoDTO> trechos = new ArrayList<>();
    private List<BagagemTrajetoAereoDTO> bagagens = new ArrayList<>();
}
