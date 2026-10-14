package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.documento.ESimDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ESimRelatorioDTO {

    private String operadora;
    private String plano;
    private String franquiaDados;
    private String cobertura;
    private Integer duracaoDias;
    private String instrucoesInstalacao;
    private String observacoes;
    private String codigosAtivacao;
    private int quantidade;
    private BigDecimal valorTotal;

    public ESimRelatorioDTO(ESimDTO esim) {
        if (esim == null) {
            return;
        }
        operadora = esim.getOperadora();
        plano = esim.getPlano();
        franquiaDados = esim.getFranquiaDados();
        cobertura = esim.getCobertura();
        duracaoDias = esim.getDuracaoDias();
        instrucoesInstalacao = esim.getInstrucoesInstalacao();
        observacoes = esim.getObservacoes();
        List<String> codigos = esim.getCodigosAtivacao();
        codigosAtivacao = codigos == null ? ""
            : codigos.stream().filter(codigo -> codigo != null && !codigo.isBlank()).collect(Collectors.joining(", "));
        quantidade = esim.getQuantidade();
        valorTotal = esim.getValorTotal();
    }
}
