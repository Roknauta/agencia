package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class ServicoRelatorioDTO {

    private BigDecimal valor;
    private BigDecimal desconto;
    private Moeda moeda;
    private List<TaxaRelatorioDTO> taxas = new ArrayList<>();
    private BigDecimal valorTotal;
    private String regrasAlteracao;
    private String regrasCancelamento;
    private String observacoes;

    protected ServicoRelatorioDTO(ServicoDTO servico) {
        if (servico == null) {
            return;
        }
        valor = servico.getValor();
        desconto = servico.getDesconto();
        moeda = servico.getMoeda();
        valorTotal = servico.getValorTotal();
        regrasAlteracao = servico.getRegrasAlteracao();
        regrasCancelamento = servico.getRegrasCancelamento();
        observacoes = servico.getObservacoes();
        if (servico.getTaxas() != null) {
            taxas = servico.getTaxas().stream().filter(java.util.Objects::nonNull).map(TaxaRelatorioDTO::new).toList();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class TaxaRelatorioDTO {

        private String tipo;
        private BigDecimal valor;
        private Moeda moeda;

        public TaxaRelatorioDTO(TaxaDTO taxa) {
            if (taxa != null) {
                tipo = taxa.getTipoTaxa() == null ? null : taxa.getTipoTaxa().getNome();
                valor = taxa.getValor();
                moeda = taxa.getMoeda();
            }
        }
    }

    public String getResumoFinanceiro() {
        String simbolo = moeda == null ? "R$" : moeda.getSimbolo();
        java.text.NumberFormat formato =
            java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("pt-BR"));
        StringBuilder resumo = new StringBuilder();
        adicionar(resumo, "Valor", valor, simbolo, formato);
        adicionar(resumo, "Desconto", desconto, simbolo, formato);
        if (taxas != null) {
            for (TaxaRelatorioDTO taxa : taxas) {
                if (taxa != null) {
                    adicionar(resumo, "Taxa " + (taxa.getTipo() == null ? "" : taxa.getTipo()), taxa.getValor(),
                        taxa.getMoeda() == null ? simbolo : taxa.getMoeda().getSimbolo(), formato);
                }
            }
        }
        adicionar(resumo, "Total", valorTotal, simbolo, formato);
        return resumo.toString();
    }

    public String getResumoCondicoes() {
        StringBuilder resumo = new StringBuilder();
        adicionarCondicao(resumo, "Alterações", regrasAlteracao);
        adicionarCondicao(resumo, "Cancelamento", regrasCancelamento);
        adicionarCondicao(resumo, "Observações", observacoes);
        return resumo.toString();
    }

    public List<AtributoRelatorioDTO> getValoresRelatorio() {
        List<AtributoRelatorioDTO> linhas = new ArrayList<>();
        String simbolo = moeda == null ? "R$" : moeda.getSimbolo();
        java.text.NumberFormat formato =
            java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("pt-BR"));
        if (valor != null) {
            linhas.add(new AtributoRelatorioDTO("Valor", dinheiro(valor, simbolo, formato)));
        }
        if (desconto != null && desconto.signum() != 0) {
            linhas.add(new AtributoRelatorioDTO("Desconto", dinheiro(desconto, simbolo, formato)));
        }
        if (taxas != null) {
            for (TaxaRelatorioDTO taxa : taxas) {
                if (taxa != null && taxa.getValor() != null) {
                    String moedaTaxa = taxa.getMoeda() == null ? simbolo : taxa.getMoeda().getSimbolo();
                    linhas.add(new AtributoRelatorioDTO("Taxa " + java.util.Objects.toString(taxa.getTipo(), ""),
                        dinheiro(taxa.getValor(), moedaTaxa, formato)));
                }
            }
        }
        if (valorTotal != null) {
            linhas.add(new AtributoRelatorioDTO("Valor Total", dinheiro(valorTotal, simbolo, formato)));
        }
        return linhas;
    }

    public List<AtributoRelatorioDTO> getCondicoesRelatorio() {
        return AtributoRelatorioDTO.lista(AtributoRelatorioDTO.campo("Alterações", regrasAlteracao),
            AtributoRelatorioDTO.campo("Cancelamento", regrasCancelamento),
            AtributoRelatorioDTO.campo("Observações", observacoes));
    }

    private static String dinheiro(BigDecimal valor, String simbolo, java.text.NumberFormat formato) {
        return simbolo + " " + formato.format(valor);
    }

    private static void adicionarCondicao(StringBuilder resumo, String rotulo, String valor) {
        if (valor != null && !valor.isBlank()) {
            if (!resumo.isEmpty()) {
                resumo.append("\n");
            }
            resumo.append(rotulo).append(": ").append(valor);
        }
    }

    private static void adicionar(StringBuilder resumo, String rotulo, BigDecimal valor, String simbolo,
        java.text.NumberFormat formato) {
        if (valor != null) {
            if (!resumo.isEmpty()) {
                resumo.append("\n");
            }
            resumo.append(rotulo).append(": ").append(simbolo).append(" ").append(formato.format(valor));
        }
    }
}
