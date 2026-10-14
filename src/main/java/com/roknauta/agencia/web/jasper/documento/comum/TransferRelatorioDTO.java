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
public class TransferRelatorioDTO extends ServicoRelatorioDTO {

    private String prestador;
    private String tipo;
    private String localizador;
    private String origem;
    private String destino;
    private LocalDateTime dataHora;
    private String fusoHorario;
    private int passageiros;
    private String resumoPercursos;
    private String resumoDatas;
    private String resumoValores;
    private int quantidade;

    public TransferRelatorioDTO(TransferDTO transfer) {
        super(transfer);
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
        setObservacoes(itens.stream().map(TransferRelatorioDTO::getObservacoes).filter(java.util.Objects::nonNull)
            .collect(Collectors.joining("\n")));
        setValorTotal(itens.stream().map(TransferRelatorioDTO::getValorTotal).filter(java.util.Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        if (!itens.isEmpty()) {
            setMoeda(itens.get(0).getMoeda());
        }
        resumoPercursos = itens.stream().map(item -> item.getOrigem() + " para " + item.getDestino())
            .collect(Collectors.joining(" • "));
        resumoDatas = itens.stream().filter(item -> item.getDataHora() != null)
            .map(item -> item.getDataHora().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            .collect(Collectors.joining(" • "));
        resumoValores = itens.stream().map(item -> textoValor(item.getPrestador(), item.getValorTotal()))
            .filter(texto -> !texto.isBlank()).collect(Collectors.joining(" • "));
    }

    private String textoValor(String prestador, java.math.BigDecimal valor) {
        if (valor == null) {
            return "";
        }
        String rotulo = prestador == null || prestador.isBlank() ? "Transfer" : prestador;
        String simbolo = getMoeda() == null ? "R$" : getMoeda().getSimbolo();
        return rotulo + ": " + simbolo + " "
            + java.text.NumberFormat.getNumberInstance(java.util.Locale.forLanguageTag("pt-BR")).format(valor);
    }
}
