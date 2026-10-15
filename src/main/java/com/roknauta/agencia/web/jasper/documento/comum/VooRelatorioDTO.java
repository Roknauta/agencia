package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.Duration;
import java.time.LocalDateTime;
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
    private String localizador;
    private String cabine;
    private String tarifa;

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
        tarifa = trecho.getTarifaAerea();
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
