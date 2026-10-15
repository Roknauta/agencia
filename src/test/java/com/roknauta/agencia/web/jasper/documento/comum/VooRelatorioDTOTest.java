package com.roknauta.agencia.web.jasper.documento.comum;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import com.roknauta.agencia.web.jasper.DadosRelatoriosTeste;

class VooRelatorioDTOTest {

    @Test
    void calculaDuracaoDoVooConsiderandoFusoDosAeroportos() {
        TrechoAereoDTO trecho = DadosRelatoriosTeste.trecho("AB123", "JFK", "GRU", LocalTime.of(22, 0),
            LocalTime.of(8, 0), LocalDate.of(2027, 1, 15));
        trecho.getVoo().getOrigem().getCidadeAtendida().setFusoHorario("America/New_York");
        trecho.getVoo().getDestino().getCidadeAtendida().setFusoHorario("America/Sao_Paulo");

        VooRelatorioDTO voo = new VooRelatorioDTO(trecho);

        assertEquals("8h 0min", voo.getDuracaoFormatada());
        assertEquals(LocalDate.of(2027, 1, 15).atTime(22, 0), voo.getSaida());
        assertEquals(LocalDate.of(2027, 1, 16).atTime(8, 0), voo.getChegada());
    }

    @Test
    void calculaConexaoEntreFusosDiferentes() {
        TrechoAereoDTO primeiroTrecho = DadosRelatoriosTeste.trecho("AB123", "JFK", "GRU", LocalTime.of(22, 0),
            LocalTime.of(8, 0), LocalDate.of(2027, 1, 15));
        primeiroTrecho.getVoo().getOrigem().getCidadeAtendida().setFusoHorario("America/New_York");
        primeiroTrecho.getVoo().getDestino().getCidadeAtendida().setFusoHorario("America/Sao_Paulo");

        TrechoAereoDTO proximoTrecho = DadosRelatoriosTeste.trecho("AB456", "GRU", "LIS", LocalTime.of(10, 0),
            LocalTime.of(22, 0), LocalDate.of(2027, 1, 16));
        proximoTrecho.getVoo().getOrigem().getCidadeAtendida().setFusoHorario("America/Sao_Paulo");
        proximoTrecho.getVoo().getDestino().getCidadeAtendida().setFusoHorario("Europe/Lisbon");

        List<VooRelatorioDTO> voos = List.of(new VooRelatorioDTO(primeiroTrecho), new VooRelatorioDTO(proximoTrecho));
        voos.get(0).calcularConexaoPara(voos.get(1));

        assertEquals("2h 0min", voos.get(0).getConexaoFormatada());
    }
}
