package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private List<BagagemRelatorioDTO> bagagens = new ArrayList<>();

    public TrajetoAereoRelatorioDTO(TrajetoAereoDTO trajeto) {
        super(trajeto);
        if (trajeto == null) {
            return;
        }
        sentido = trajeto.getSentido() == null ? null : trajeto.getSentido().name();
        if (trajeto.getBagagens() != null) {
            bagagens = trajeto.getBagagens().stream().map(BagagemRelatorioDTO::new).toList();
        }
        if (trajeto.getTrechos() != null) {
            voos = trajeto.getTrechos().stream().map(VooRelatorioDTO::new).toList();
            for (int indice = 0; indice < voos.size(); indice++) {
                VooRelatorioDTO voo = voos.get(indice);
                VooRelatorioDTO proximoVoo = indice + 1 < voos.size() ? voos.get(indice + 1) : null;
                voo.calcularConexaoPara(proximoVoo);
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
        return bagagens;
    }

    public String getResumoBagagens() {
        Map<String, Integer> quantidadePorTipo = new LinkedHashMap<>();
        for (BagagemRelatorioDTO bagagem : bagagens) {
            if (bagagem.getDescricaoRelatorio() != null && !bagagem.getDescricaoRelatorio().isBlank()) {
                quantidadePorTipo.merge(bagagem.getDescricaoRelatorio(),
                    bagagem.getQuantidade() == null ? 0 : bagagem.getQuantidade(), Integer::sum);
            }
        }
        return quantidadePorTipo.entrySet().stream().map(item -> item.getValue() + " " + item.getKey())
            .collect(Collectors.joining(", "));
    }

    public String getResumoCondicoesComTarifas() {
        return getResumoCondicoes();
    }

    public List<AtributoRelatorioDTO> getAtributosRelatorio() {
        List<AtributoRelatorioDTO> atributos = new ArrayList<>();
        atributos.add(AtributoRelatorioDTO.campo("Sentido", sentido));
        return atributos.stream().filter(java.util.Objects::nonNull).toList();
    }

}
