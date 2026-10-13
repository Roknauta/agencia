package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.roknauta.agencia.dto.documento.TrajetoRodoviarioDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetoRodoviarioRelatorioDTO {

    private String transportadora;
    private String origem;
    private String destino;
    private String terminalEmbarque;
    private String terminalDesembarque;
    private String categoria;
    private String localizador;
    private String bagagem;
    private LocalDateTime saida;
    private LocalDateTime chegada;
    private BigDecimal valorTotal;

    public TrajetoRodoviarioRelatorioDTO(TrajetoRodoviarioDTO trajeto) {
        if (trajeto == null) {
            return;
        }
        origem = new CidadeRelatorioDTO(trajeto.getOrigem()).getNome();
        destino = new CidadeRelatorioDTO(trajeto.getDestino()).getNome();
        transportadora = trajeto.getTransportadora();
        terminalEmbarque = trajeto.getTerminalEmbarque();
        terminalDesembarque = trajeto.getTerminalDesembarque();
        categoria = trajeto.getCategoria() == null ? null : trajeto.getCategoria().name();
        localizador = trajeto.getLocalizador();
        bagagem = trajeto.getBagagem();
        saida = trajeto.getSaida();
        chegada = trajeto.getChegada();
        valorTotal = trajeto.getValorTotal();
    }
}
