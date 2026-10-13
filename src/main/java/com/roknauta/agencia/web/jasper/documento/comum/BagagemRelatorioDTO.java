package com.roknauta.agencia.web.jasper.documento.comum;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.roknauta.agencia.dto.documento.BagagemTrechoAereoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BagagemRelatorioDTO {

    private String descricaoRelatorio;
    private String dimensoes;
    private Double pesoMaximo;
    private Integer quantidade;

    public BagagemRelatorioDTO(BagagemTrechoAereoDTO bagagem) {
        if (bagagem == null) {
            return;
        }
        quantidade = bagagem.getQuantidade();
        var referencia = bagagem.getBagagemCompanhiaAerea();
        if (referencia != null) {
            descricaoRelatorio =
                referencia.getTipoBagagem() == null ? bagagem.getDescricao() : referencia.getTipoBagagem().getLabel();
            dimensoes = Stream.of(referencia.getAlturaCm(), referencia.getLarguraCm(), referencia.getProfundidadeCm())
                .map(valor -> valor == null ? null : String.valueOf(valor)).filter(Objects::nonNull)
                .collect(Collectors.joining(" x "));
            pesoMaximo = referencia.getPesoMaximo();
        } else {
            descricaoRelatorio = bagagem.getDescricao();
        }
    }
}
