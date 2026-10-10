package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanhiaAereaDTO extends BaseDTO {

    private String nome;
    private PaisDTO pais;
}
