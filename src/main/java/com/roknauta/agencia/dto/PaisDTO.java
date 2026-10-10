package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class PaisDTO extends BaseDTO {

    private String nome;
    private String sigla;
}
