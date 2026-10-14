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
public class TrajetoAereoRelatorioDTO extends ServicoRelatorioDTO {

    private String sentido;
    private List<VooRelatorioDTO> voos = new ArrayList<>();

    public TrajetoAereoRelatorioDTO(TrajetoAereoDTO trajeto) {
        super(trajeto);
        if (trajeto == null) {
            return;
        }
        sentido = trajeto.getSentido() == null ? null : trajeto.getSentido().name();
        if (trajeto.getTrechos() != null) {
            voos = trajeto.getTrechos().stream().map(VooRelatorioDTO::new).toList();
            for (int indice = 0; indice < voos.size(); indice++) {
                VooRelatorioDTO proximoVoo = indice + 1 < voos.size() ? voos.get(indice + 1) : null;
                voos.get(indice).calcularConexaoPara(proximoVoo);
            }
        }
    }

    public String getResumoCabine() {
        return voos.stream().map(VooRelatorioDTO::getCabine).filter(texto -> texto != null && !texto.isBlank())
            .distinct().collect(Collectors.joining(" • "));
    }

    public String getResumoTarifa() {
        return voos.stream().map(VooRelatorioDTO::getTarifa).filter(texto -> texto != null && !texto.isBlank())
            .distinct().collect(Collectors.joining(" • "));
    }

    public List<BagagemRelatorioDTO> getBagagens() {
        return voos.stream().filter(voo -> voo.getBagagens() != null).flatMap(voo -> voo.getBagagens().stream())
            .toList();
    }
}
