package com.roknauta.agencia.web.jasper.documento.comum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BlocoTextoRelatorioDTO {
    private String titulo;
    private String texto;
    private Boolean destaque;

    public BlocoTextoRelatorioDTO(String titulo, String texto, Boolean destaque) {
        this.titulo = titulo;
        this.texto = texto;
        this.destaque = destaque;
    }
}
