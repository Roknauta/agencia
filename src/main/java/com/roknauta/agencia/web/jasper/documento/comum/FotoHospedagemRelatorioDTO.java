package com.roknauta.agencia.web.jasper.documento.comum;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FotoHospedagemRelatorioDTO {
    private InputStream esquerda;
    private InputStream direita;

    public FotoHospedagemRelatorioDTO(FotoHospedagemDTO esquerda, FotoHospedagemDTO direita) {
        if (esquerda != null && esquerda.getConteudo() != null) {
            this.esquerda = new ByteArrayInputStream(esquerda.getConteudo());
        }
        if (direita != null && direita.getConteudo() != null) {
            this.direita = new ByteArrayInputStream(direita.getConteudo());
        }
    }
}
