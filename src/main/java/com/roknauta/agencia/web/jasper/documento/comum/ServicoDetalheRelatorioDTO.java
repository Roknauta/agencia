package com.roknauta.agencia.web.jasper.documento.comum;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServicoDetalheRelatorioDTO {

    private String titulo;
    private List<AtributoRelatorioDTO> valores = List.of();
    private List<AtributoRelatorioDTO> condicoes = List.of();
    private List<VooRelatorioDTO> voos = List.of();
    private ServicoRelatorioDTO dados;

    public ServicoDetalheRelatorioDTO(String titulo, ServicoRelatorioDTO servico) {
        this.titulo = titulo;
        this.dados = servico;
        valores = servico == null ? List.of() : new ArrayList<>(servico.getValoresRelatorio());
        condicoes = servico == null ? List.of() : new ArrayList<>(servico.getCondicoesRelatorio());
        if (servico instanceof TrajetoAereoRelatorioDTO trajetoAereo) {
            voos = trajetoAereo.getVoos();
        }
    }
}
