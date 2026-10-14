package com.roknauta.agencia.web.jasper.documento.comum;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtributoRelatorioDTO {

    private String rotulo;
    private String valor;

    public AtributoRelatorioDTO(String rotulo, Object valor) {
        this.rotulo = rotulo;
        this.valor = Objects.toString(valor, "");
    }

    public static AtributoRelatorioDTO campo(String rotulo, Object valor) {
        if (valor == null || valor instanceof String texto && texto.isBlank()) {
            return null;
        }
        return new AtributoRelatorioDTO(rotulo, valor);
    }

    public static List<AtributoRelatorioDTO> lista(AtributoRelatorioDTO... atributos) {
        return Arrays.stream(atributos).filter(Objects::nonNull).toList();
    }
}
