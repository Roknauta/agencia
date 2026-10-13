package com.roknauta.agencia.web.jasper.documento.comum;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.roknauta.agencia.dto.CidadeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CidadeRelatorioDTO {

    private String nome;
    private String codigoIata;

    public CidadeRelatorioDTO(CidadeDTO cidade) {
        if (cidade == null) {
            return;
        }
        nome = Stream
            .of(cidade.getNome(), cidade.getEstado(), cidade.getPais() == null ? null : cidade.getPais().getNome())
            .filter(valor -> valor != null && !valor.isBlank()).collect(Collectors.joining(", "));
    }
}
