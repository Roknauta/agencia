package com.roknauta.agencia.web.jasper.documento.comum;

import com.roknauta.agencia.dto.documento.BagagemTrajetoAereoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BagagemRelatorioDTO {

    private String descricaoRelatorio;
    private Integer quantidade;

    public BagagemRelatorioDTO(BagagemTrajetoAereoDTO bagagem) {
        if (bagagem != null) {
            if (bagagem.getTipoBagagem() != null) {
                descricaoRelatorio = switch (bagagem.getTipoBagagem()) {
                    case ITEM_PESSOAL -> "Item Pessoal";
                    case BAGAGEM_DE_MAO -> "Mala de Mão";
                    case BAGAGEM_DESPACHADA -> "Bagagem Despachada";
                    case BAGAGEM_ESPECIAL -> "Bagagem Especial";
                };
            }
            quantidade = bagagem.getQuantidade();
        }
    }
}
