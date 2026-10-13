package com.roknauta.agencia.web.jasper.documento.comum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompanhiaAereaRelatorioDTO {

    private String nome;

    public CompanhiaAereaRelatorioDTO(String nome) {
        this.nome = nome;
    }
}
