package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HotelDTO extends BaseDTO {

    private String nome;
    private CidadeDTO cidade;
    private String endereco;
}
