package com.roknauta.agencia.dto.documento;

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
public class ESimDTO extends BaseDTO {

    private String operadora;
    private String plano;
    private String franquiaDados;
    private String cobertura;
    private Integer duracaoDias;
    private String instrucoesInstalacao;
    private List<String> codigosAtivacao = new ArrayList<>();
    private int quantidade = 1;
    private BigDecimal valorTotal;
}
