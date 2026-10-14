package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.documento.AluguelVeiculoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AluguelVeiculoRelatorioDTO {

    private String locadora;
    private String categoria;
    private String condutor;
    private String localizador;
    private String localRetirada;
    private String localDevolucao;
    private String coberturas;
    private String condicoes;
    private String observacoes;
    private LocalDateTime retirada;
    private LocalDateTime devolucao;
    private String fusoHorarioRetirada;
    private String fusoHorarioDevolucao;
    private BigDecimal valorTotal;
    private String resumoRetirada;
    private String resumoDevolucao;
    private String resumoValores;

    public AluguelVeiculoRelatorioDTO(AluguelVeiculoDTO aluguel) {
        if (aluguel == null) {
            return;
        }
        locadora = aluguel.getLocadora() == null ? null : aluguel.getLocadora().getLabel();
        categoria = aluguel.getCategoria() == null ? null : aluguel.getCategoria().getDescricao();
        List<String> condutores = aluguel.getCondutores();
        condutor = condutores == null ? null
            : condutores.stream().filter(nome -> nome != null && !nome.isBlank()).collect(Collectors.joining(", "));
        localizador = aluguel.getLocalizador();
        localRetirada = aluguel.getLocalRetirada();
        localDevolucao = aluguel.getLocalDevolucao();
        coberturas = aluguel.getCoberturas();
        condicoes = aluguel.getCondicoes();
        observacoes = aluguel.getObservacoes();
        retirada = aluguel.getRetirada();
        devolucao = aluguel.getDevolucao();
        fusoHorarioRetirada = aluguel.getFusoHorarioRetirada();
        fusoHorarioDevolucao = aluguel.getFusoHorarioDevolucao();
        valorTotal = aluguel.getValorTotal();
        resumoRetirada = retirada == null ? "" : retirada.toString();
        resumoDevolucao = devolucao == null ? "" : devolucao.toString();
    }

    public AluguelVeiculoRelatorioDTO(List<AluguelVeiculoRelatorioDTO> alugueis) {
        if (alugueis == null || alugueis.isEmpty()) {
            return;
        }
        List<AluguelVeiculoRelatorioDTO> itens = alugueis.stream().filter(java.util.Objects::nonNull).toList();
        locadora = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getLocadora).toList());
        categoria = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getCategoria).toList());
        condutor = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getCondutor).toList());
        localizador = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getLocalizador).toList());
        localRetirada = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getLocalRetirada).toList());
        localDevolucao = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getLocalDevolucao).toList());
        coberturas = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getCoberturas).toList());
        condicoes = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getCondicoes).toList());
        observacoes = juntar(itens.stream().map(AluguelVeiculoRelatorioDTO::getObservacoes).toList());
        valorTotal = itens.stream().map(AluguelVeiculoRelatorioDTO::getValorTotal).filter(java.util.Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        resumoRetirada = itens.stream().map(item -> dataHora(item.getRetirada())).filter(texto -> !texto.isBlank())
            .collect(Collectors.joining(" • "));
        resumoDevolucao = itens.stream().map(item -> dataHora(item.getDevolucao())).filter(texto -> !texto.isBlank())
            .collect(Collectors.joining(" • "));
        resumoValores = itens.stream().map(AluguelVeiculoRelatorioDTO::textoValor).filter(texto -> !texto.isBlank())
            .collect(Collectors.joining(" • "));
    }

    private static String juntar(List<String> valores) {
        return valores.stream().filter(valor -> valor != null && !valor.isBlank()).collect(Collectors.joining(" • "));
    }

    private static String dataHora(LocalDateTime valor) {
        return valor == null ? "" : valor.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private static String textoValor(AluguelVeiculoRelatorioDTO aluguel) {
        if (aluguel.getValorTotal() == null) {
            return "";
        }
        String rotulo = java.util.stream.Stream.of(aluguel.getLocadora(), aluguel.getCategoria())
            .filter(valor -> valor != null && !valor.isBlank()).collect(Collectors.joining(" "));
        return (rotulo.isBlank() ? "Aluguel de veículo" : rotulo) + ": " + java.text.NumberFormat
            .getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR")).format(aluguel.getValorTotal());
    }
}
