package com.roknauta.agencia.web.jasper.documento.emissao;

import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BilheteAereoRelatorioDTO {
    private String nome;
    private String bilhete;
    private String assento;
    private String voo;

    public BilheteAereoRelatorioDTO(BilheteAereoDTO bilheteAereo) {
        if (bilheteAereo == null) {
            return;
        }
        nome = bilheteAereo.getViajante() == null ? null : bilheteAereo.getViajante().getNome();
        bilhete = bilheteAereo.getNumeroBilhete();
        assento = bilheteAereo.getAssento();
        voo = bilheteAereo.getTrechoAereo() == null || bilheteAereo.getTrechoAereo().getVoo() == null
                ? null : bilheteAereo.getTrechoAereo().getVoo().getNumero();
    }
}
