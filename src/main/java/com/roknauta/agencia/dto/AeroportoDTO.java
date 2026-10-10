package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AeroportoDTO extends BaseDTO {

    private String nome;
    private CidadeDTO cidadeAtendida;
    private String codigoIata;

    public String getCidadeAndIata() {
        return this.cidadeAtendida.getNome()+" - "+this.codigoIata;
    }
}
