package com.roknauta.agencia.dto.documento;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ESimDTO extends ServicoDTO {

    private String operadora;
    private String plano;
    private String franquiaDados;
    private String cobertura;
    private Integer duracaoDias;
    private String instrucoesInstalacao;
    private List<String> codigosAtivacao = new ArrayList<>();
    private int quantidade = 1;
}
