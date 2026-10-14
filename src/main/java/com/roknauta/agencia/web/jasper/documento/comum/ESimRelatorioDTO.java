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
public class ESimRelatorioDTO extends ServicoRelatorioDTO {

    private String operadora;
    private String plano;
    private String franquiaDados;
    private String cobertura;
    private Integer duracaoDias;
    private String instrucoesInstalacao;
    private String codigosAtivacao;
    private int quantidade;

    public ESimRelatorioDTO(ESimDTO esim) {
        super(esim);
        if (esim == null) {
            return;
        }
        operadora = esim.getOperadora();
        plano = esim.getPlano();
        franquiaDados = esim.getFranquiaDados();
        cobertura = esim.getCobertura();
        duracaoDias = esim.getDuracaoDias();
        instrucoesInstalacao = esim.getInstrucoesInstalacao();
        List<String> codigos = esim.getCodigosAtivacao();
        codigosAtivacao = codigos == null ? ""
            : codigos.stream().filter(codigo -> codigo != null && !codigo.isBlank()).collect(Collectors.joining(", "));
        quantidade = esim.getQuantidade();
    }

    public List<AtributoRelatorioDTO> getAtributosRelatorio() {
        return AtributoRelatorioDTO.lista(AtributoRelatorioDTO.campo("Operadora", operadora),
            AtributoRelatorioDTO.campo("Plano", plano), AtributoRelatorioDTO.campo("Franquia", franquiaDados),
            AtributoRelatorioDTO.campo("Cobertura", cobertura),
            AtributoRelatorioDTO.campo("Duração", duracaoDias == null ? null : duracaoDias + " dias"),
            AtributoRelatorioDTO.campo("Quantidade", quantidade),
            AtributoRelatorioDTO.campo("Códigos de ativação", codigosAtivacao),
            AtributoRelatorioDTO.campo("Instruções de instalação", instrucoesInstalacao));
    }
}
