package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VooRelatorioDTO {

    private CompanhiaAereaRelatorioDTO companhia;
    private String numero;
    private String origem;
    private LocalDateTime saida;
    private String destino;
    private LocalDateTime chegada;
    private String duracaoFormatada;
    private String conexaoFormatada;
    private Boolean direto;
    private String regraCancelamento;
    private String regraAlteracao;
    private String outrasInformacoesTarifarias;
    private String localizador;
    private String cabine;
    private String tarifa;
    private List<BagagemRelatorioDTO> bagagens = new ArrayList<>();

    public VooRelatorioDTO(TrechoAereoDTO trecho) {
        if (trecho == null || trecho.getVoo() == null) {
            return;
        }
        var voo = trecho.getVoo();
        companhia =
            voo.getCompanhiaAerea() == null ? null : new CompanhiaAereaRelatorioDTO(voo.getCompanhiaAerea().getNome());
        numero = voo.getNumero();
        localizador = trecho.getLocalizador();
        cabine = trecho.getCabine() == null ? null : trecho.getCabine().name();
        origem = formatarAeroporto(voo.getOrigem());
        destino = formatarAeroporto(voo.getDestino());
        if (trecho.getTarifaAerea() != null) {
            tarifa = trecho.getTarifaAerea().getNome();
            regraCancelamento = trecho.getTarifaAerea().getRegraCancelamento();
            regraAlteracao = trecho.getTarifaAerea().getRegraAlteracao();
            outrasInformacoesTarifarias = trecho.getTarifaAerea().getOutrasInformacoes();
        }
        if (trecho.getBagagens() != null) {
            bagagens = trecho.getBagagens().stream().map(BagagemRelatorioDTO::new).toList();
        }
        if (trecho.getDataPartida() != null && voo.getPartida() != null) {
            saida = trecho.getDataPartida().atTime(voo.getPartida());
        }
        if (trecho.getDataChegada() != null && voo.getChegada() != null) {
            chegada = trecho.getDataChegada().atTime(voo.getChegada());
        }
        if (saida != null && chegada != null) {
            duracaoFormatada = formatarDuracao(Duration.between(saida, chegada));
        }
    }

    private static String formatarDuracao(Duration duracao) {
        long minutos = Math.max(0, duracao.toMinutes());
        return (minutos / 60) + "h " + (minutos % 60) + "min";
    }

    public void calcularConexaoPara(VooRelatorioDTO proximoVoo) {
        if (proximoVoo == null || chegada == null || proximoVoo.getSaida() == null) {
            conexaoFormatada = "";
            return;
        }
        conexaoFormatada = formatarDuracao(Duration.between(chegada, proximoVoo.getSaida()));
    }

    public String getResumoBagagens() {
        Map<String, Integer> quantidadePorTipo = new LinkedHashMap<>();
        for (BagagemRelatorioDTO bagagem : bagagens) {
            String tipo = tipoBagagem(bagagem.getDescricaoRelatorio());
            if (tipo != null && !tipo.isBlank()) {
                quantidadePorTipo.merge(tipo, bagagem.getQuantidade() == null ? 0 : bagagem.getQuantidade(),
                    Integer::sum);
            }
        }
        return quantidadePorTipo.entrySet().stream().map(item -> item.getValue() + " " + item.getKey())
            .collect(Collectors.joining(", "));
    }

    private static String tipoBagagem(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }
        return switch (descricao.trim().toLowerCase(Locale.forLanguageTag("pt-BR"))) {
            case "bagagem despachada" -> "Bagagem Despachada";
            case "bagagem de mão", "mala de mão" -> "Mala de Mão";
            case "item pessoal" -> "Item Pessoal";
            case "bagagem especial" -> "Bagagem Especial";
            default -> descricao;
        };
    }

    private static String formatarAeroporto(AeroportoDTO aeroporto) {
        if (aeroporto == null) {
            return null;
        }
        String nome = aeroporto.getNome();
        String codigoIata = aeroporto.getCodigoIata();
        if (nome == null || nome.isBlank()) {
            return codigoIata;
        }
        return codigoIata == null || codigoIata.isBlank() ? nome : nome + " (" + codigoIata + ")";
    }
}
