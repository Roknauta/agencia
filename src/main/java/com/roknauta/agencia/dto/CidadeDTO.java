package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CidadeDTO extends BaseDTO {

    private String nome;
    private String estado;
    private PaisDTO pais;
}
