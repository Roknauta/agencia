package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetoAereoRelatorioDTO {
    private String sentido;
    private BigDecimal valorTotal;
    private List<ReservaAereaRelatorioDTO> reservas = new ArrayList<>();

    public TrajetoAereoRelatorioDTO(TrajetoAereoDTO trajeto) {
        if (trajeto == null) {
            return;
        }
        sentido = trajeto.getSentido() == null ? null : trajeto.getSentido().name();
        valorTotal = trajeto.getValorTotal();
        if (trajeto.getReservas() != null) {
            reservas = trajeto.getReservas().stream().map(ReservaAereaRelatorioDTO::new).toList();
        }
    }

    public List<VooRelatorioDTO> getVoos() {
        return reservas.stream().filter(reserva -> reserva.getVoos() != null)
                .flatMap(reserva -> reserva.getVoos().stream()).toList();
    }

    public String getResumoCabine() {
        return reservas.stream().map(ReservaAereaRelatorioDTO::getCabine)
                .filter(texto -> texto != null && !texto.isBlank()).distinct()
                .collect(Collectors.joining(" • "));
    }

    public String getResumoTarifa() {
        return reservas.stream().map(ReservaAereaRelatorioDTO::getTarifa)
                .filter(texto -> texto != null && !texto.isBlank()).distinct()
                .collect(Collectors.joining(" • "));
    }

    public List<BagagemRelatorioDTO> getBagagens() {
        return reservas.stream().filter(reserva -> reserva.getBagagens() != null)
                .flatMap(reserva -> reserva.getBagagens().stream()).toList();
    }
}
