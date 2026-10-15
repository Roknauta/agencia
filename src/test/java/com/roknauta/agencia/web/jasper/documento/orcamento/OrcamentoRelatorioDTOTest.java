package com.roknauta.agencia.web.jasper.documento.orcamento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.ESimDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.TransferDTO;
import com.roknauta.agencia.web.jasper.DadosRelatoriosTeste;

class OrcamentoRelatorioDTOTest {

    @Test
    void deveMapearTodosOsDadosECombinarTodosOsServicos() {
        OrcamentoDTO orcamento = DadosRelatoriosTeste.orcamentoCompleto();
        OrcamentoRelatorioDTO relatorio = new OrcamentoRelatorioDTO(orcamento);

        assertEquals("DOC-TESTE-2027-001", relatorio.getNumero());
        assertEquals("Família Exemplo", relatorio.getCliente());
        assertEquals("Ana Consultora", relatorio.getConsultor());
        assertEquals("Belo Horizonte, MG, Brasil", relatorio.getOrigem());
        assertEquals("Lisboa, Portugal", relatorio.getDestino());
        assertEquals("Apresentar-se com antecedência e conferir os documentos.", relatorio.getObservacoes());
        assertEquals(4, relatorio.getQuantidadePassageiros());
        assertEquals(2, relatorio.getTrajetosAereos().getFirst().getVoos().size());
        assertEquals("2h 45min", relatorio.getTrajetosAereos().getFirst().getVoos().getFirst().getConexaoFormatada());
        assertEquals(1, relatorio.getVolta().getVoos().size());
        assertEquals(3, relatorio.getIda().getBagagens().size());
        assertEquals(1, relatorio.getTrajetosRodoviarios().size());
        assertEquals(1, relatorio.getHospedagens().size());
        assertEquals(2, relatorio.getTransfers().size());
        assertEquals(2, relatorio.getAlugueis().size());
        assertEquals(1, relatorio.getSegurosViagem().size());
        assertEquals(1, relatorio.getESims().size());
        assertEquals(new BigDecimal("78.50"), relatorio.getValorRodoviario());
        assertEquals(new BigDecimal("350.00"), relatorio.getValorTransfer());
        assertEquals(new BigDecimal("1050.00"), relatorio.getValorAlugueis());
        assertEquals(new BigDecimal("120.00"), relatorio.getValorSeguros());
        assertEquals(new BigDecimal("60.00"), relatorio.getValorESims());
        assertEquals(new BigDecimal("8008.50"), relatorio.getTotal());
        assertEquals(new BigDecimal("2002.13"), relatorio.getValorPorPessoa());
        assertEquals("Cancelamento sujeito a multa do fornecedor.",
            relatorio.getIda().getVoos().getFirst().getRegraCancelamento());
    }

    @Test
    void deveCalcularCombinacoesDeServicosSemSomarItensAusentes() {
        OrcamentoDTO aereoComHotel = DadosRelatoriosTeste.orcamentoCompleto();
        aereoComHotel.setTransfers(List.of());
        aereoComHotel.setAlugueis(List.of());
        aereoComHotel.setSegurosViagem(List.of());
        aereoComHotel.setESims(List.of());
        aereoComHotel.setTrajetosRodoviarios(List.of());
        OrcamentoRelatorioDTO primeiro = new OrcamentoRelatorioDTO(aereoComHotel);
        assertEquals(new BigDecimal("6350.00"), primeiro.getTotal());
        assertEquals(BigDecimal.ZERO, primeiro.getValorTransfer());
        assertEquals(2, primeiro.getIda().getVoos().size());

        OrcamentoDTO apenasTransferESim = new OrcamentoDTO();
        TransferDTO transfer = new TransferDTO();
        transfer.setValor(new BigDecimal("80.00"));
        apenasTransferESim.setTransfers(List.of(transfer));
        ESimDTO esim = new ESimDTO();
        esim.setValor(new BigDecimal("30.00"));
        apenasTransferESim.setESims(List.of(esim));
        OrcamentoRelatorioDTO segundo = new OrcamentoRelatorioDTO(apenasTransferESim);
        assertEquals(new BigDecimal("110.00"), segundo.getTotal());
        assertEquals(1, segundo.getTransfers().size());
        assertTrue(segundo.getTrajetosAereos().isEmpty());
    }

    @Test
    void deveSuportarOrcamentoSomenteAereo() {
        OrcamentoDTO somenteAereo = new OrcamentoDTO();
        somenteAereo.setAdultos(1);
        somenteAereo.setTrajetosAereos(List.of(DadosRelatoriosTeste.trajetoIda()));

        OrcamentoRelatorioDTO relatorio = new OrcamentoRelatorioDTO(somenteAereo);

        assertEquals(new BigDecimal("2600.00"), relatorio.getTotal());
        assertEquals(2, relatorio.getIda().getVoos().size());
        assertNull(relatorio.getVolta());
        assertTrue(relatorio.getHospedagens().isEmpty());
        assertTrue(relatorio.getTransfers().isEmpty());
        assertTrue(relatorio.getAlugueis().isEmpty());
        assertTrue(relatorio.getSegurosViagem().isEmpty());
        assertTrue(relatorio.getESims().isEmpty());
    }
}
