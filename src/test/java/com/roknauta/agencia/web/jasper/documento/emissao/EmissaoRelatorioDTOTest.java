package com.roknauta.agencia.web.jasper.documento.emissao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.web.jasper.DadosRelatoriosTeste;

class EmissaoRelatorioDTOTest {

    @Test
    void deveMapearDocumentoCompletoBilhetesVoosEServicos() {
        EmissaoDTO emissao = new EmissaoDTO();
        DadosRelatoriosTeste.preencherDocumento(emissao);
        emissao.setDataDocumento(LocalDate.of(2026, 10, 9));
        ContratoDTO contrato = new ContratoDTO();
        contrato.setNumero("CTR-2026-001");
        emissao.setContrato(contrato);

        BilheteAereoDTO bilheteIda = new BilheteAereoDTO();
        bilheteIda.setViajante(emissao.getViajantes().getFirst());
        bilheteIda.setTrechoAereo(DadosRelatoriosTeste.trecho("JJ1234", "CNF", "GRU", java.time.LocalTime.of(8, 0),
            java.time.LocalTime.of(9, 15), LocalDate.of(2027, 2, 10)));
        bilheteIda.setNumeroBilhete("9571234567890");
        bilheteIda.setAssento("12A");

        BilheteAereoDTO bilheteVolta = new BilheteAereoDTO();
        bilheteVolta.setViajante(emissao.getViajantes().get(1));
        bilheteVolta.setTrechoAereo(DadosRelatoriosTeste.trecho("TP1020", "LIS", "CNF", java.time.LocalTime.of(10, 0),
            java.time.LocalTime.of(18, 20), LocalDate.of(2027, 2, 18)));
        bilheteVolta.setNumeroBilhete("0479876543210");
        bilheteVolta.setAssento("14C");
        emissao.setBilhetesAereos(List.of(bilheteIda, bilheteVolta));

        EmissaoRelatorioDTO relatorio = new EmissaoRelatorioDTO(emissao);

        assertEquals("DOC-TESTE-2027-001", relatorio.getNumero());
        assertEquals("09/10/2026 00:00", relatorio.getEmitidoEm());
        assertEquals("CTR-2026-001", relatorio.getNumeroContrato());
        assertEquals(2, relatorio.getPassageiros().size());
        assertEquals("Joana Exemplo", relatorio.getPassageiros().getFirst().getNome());
        assertEquals("9571234567890", relatorio.getPassageiros().getFirst().getBilhete());
        assertEquals("12A", relatorio.getPassageiros().getFirst().getAssento());
        assertEquals("JJ1234", relatorio.getPassageiros().getFirst().getVoo());
        assertEquals(3, relatorio.getTrechosAereos().size());
        assertEquals(1, relatorio.getHospedagens().size());
        assertEquals(2, relatorio.getTransfers().size());
        assertEquals(2, relatorio.getAlugueis().size());
        assertEquals(1, relatorio.getSegurosViagem().size());
        assertEquals(1, relatorio.getESims().size());
        assertTrue(relatorio.getRegras().contains("Alteração sujeita à diferença tarifária"));
        assertEquals("Apresentar-se com antecedência e conferir os documentos.", relatorio.getOrientacoes());
    }
}
