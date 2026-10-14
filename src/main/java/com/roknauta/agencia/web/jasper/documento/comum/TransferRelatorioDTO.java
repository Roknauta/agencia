package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.documento.TransferDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferRelatorioDTO {

    private String prestador;
    private String tipo;
    private String localizador;
    private String origem;
    private String destino;
    private LocalDateTime dataHora;
    private String fusoHorario;
    private int passageiros;
    private String observacoes;
    private java.math.BigDecimal valorTotal;
    private String resumoPercursos;
    private String resumoDatas;
    private String resumoValores;
    private int quantidade;

    public TransferRelatorioDTO(TransferDTO transfer) {
        if (transfer == null) {
            return;
        }
        prestador = transfer.getPrestador();
        tipo = transfer.getTipo() == null ? null : transfer.getTipo().name();
        localizador = transfer.getLocalizador();
        origem = transfer.getOrigem();
        destino = transfer.getDestino();
        dataHora = transfer.getDataHora();
        fusoHorario = transfer.getFusoHorario();
        passageiros = transfer.getPassageiros();
        observacoes = transfer.getObservacoes();
        valorTotal = transfer.getValorTotal();
        quantidade = 1;
        resumoPercursos = origem + " para " + destino;
        resumoDatas = dataHora == null ? "" : dataHora.toString();
    }

    public TransferRelatorioDTO(List<TransferRelatorioDTO> transfers) {
        if (transfers == null || transfers.isEmpty()) {
            return;
        }
        List<TransferRelatorioDTO> itens = transfers.stream().filter(java.util.Objects::nonNull).toList();
        quantidade = itens.size();
        prestador = itens.stream().map(TransferRelatorioDTO::getPrestador).filter(java.util.Objects::nonNull).distinct()
            .collect(Collectors.joining(", "));
        tipo = itens.stream().map(TransferRelatorioDTO::getTipo).filter(java.util.Objects::nonNull).distinct()
            .collect(Collectors.joining(", "));
        localizador = itens.stream().map(TransferRelatorioDTO::getLocalizador).filter(java.util.Objects::nonNull)
            .collect(Collectors.joining(", "));
        origem = itens.stream().map(TransferRelatorioDTO::getOrigem).filter(java.util.Objects::nonNull)
            .collect(Collectors.joining(" • "));
        destino = itens.stream().map(TransferRelatorioDTO::getDestino).filter(java.util.Objects::nonNull)
            .collect(Collectors.joining(" • "));
        passageiros = itens.stream().mapToInt(TransferRelatorioDTO::getPassageiros).sum();
        observacoes = itens.stream().map(TransferRelatorioDTO::getObservacoes).filter(java.util.Objects::nonNull)
            .collect(Collectors.joining("\n"));
        valorTotal = itens.stream().map(TransferRelatorioDTO::getValorTotal).filter(java.util.Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        resumoPercursos = itens.stream().map(item -> item.getOrigem() + " para " + item.getDestino())
            .collect(Collectors.joining(" • "));
        resumoDatas = itens.stream().filter(item -> item.getDataHora() != null)
            .map(item -> item.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .collect(Collectors.joining(" • "));
        resumoValores = itens.stream().map(item -> textoValor(item.getPrestador(), item.getValorTotal()))
            .filter(texto -> !texto.isBlank()).collect(Collectors.joining(" • "));
    }

    private static String textoValor(String prestador, java.math.BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        String rotulo = prestador == null || prestador.isBlank() ? "Transfer" : prestador;
        return rotulo + ": "
            + java.text.NumberFormat.getCurrencyInstance(java.util.Locale.forLanguageTag("pt-BR")).format(valor);
    }
}
