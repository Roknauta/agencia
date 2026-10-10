package com.roknauta.agencia.web.jasper.documento.comum;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.time.Duration;

import com.roknauta.agencia.dto.documento.ReservaDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservaAereaRelatorioDTO {
    private String localizador;
    private CompanhiaAereaRelatorioDTO companhia;
    private java.util.List<VooRelatorioDTO> voos = new ArrayList<>();
    private java.util.List<BagagemRelatorioDTO> bagagens = new ArrayList<>();
    private String cabine;
    private String tarifa;

    public ReservaAereaRelatorioDTO(ReservaDTO reserva) {
        if (reserva == null) {
            return;
        }
        localizador = reserva.getLocalizador();
        if (reserva.getTrechos() != null) {
            voos = reserva.getTrechos().stream().map(VooRelatorioDTO::new).toList();
            boolean direto = voos.size() == 1;
            voos.forEach(voo -> voo.setDireto(direto));
            for (int i = 0; i + 1 < voos.size(); i++) {
                VooRelatorioDTO anterior = voos.get(i);
                VooRelatorioDTO proximo = voos.get(i + 1);
                if (anterior.getChegada() != null && proximo.getSaida() != null) {
                    long minutos = Math.max(0, Duration.between(anterior.getChegada(), proximo.getSaida()).toMinutes());
                    anterior.setConexaoFormatada((minutos / 60) + "h " + (minutos % 60) + "min");
                }
            }
            String nomeCompanhia = reserva.getTrechos().stream()
                    .map(trecho -> trecho.getVoo() == null || trecho.getVoo().getCompanhiaAerea() == null
                            ? null : trecho.getVoo().getCompanhiaAerea().getNome())
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.joining(", "));
            companhia = nomeCompanhia.isBlank() ? null : new CompanhiaAereaRelatorioDTO(nomeCompanhia);
            cabine = reserva.getTrechos().stream()
                    .map(trecho -> trecho.getCabine() == null ? null : trecho.getCabine().name())
                    .filter(texto -> texto != null && !texto.isBlank())
                    .distinct()
                    .collect(Collectors.joining(" • "));
            tarifa = reserva.getTrechos().stream()
                    .map(trecho -> trecho.getTarifaAerea() == null ? null : trecho.getTarifaAerea().getNome())
                    .filter(texto -> texto != null && !texto.isBlank())
                    .distinct()
                    .collect(Collectors.joining(" • "));
        }
        if (reserva.getBagagens() != null) {
            bagagens = reserva.getBagagens().stream().map(BagagemRelatorioDTO::new).toList();
        }
    }
}
