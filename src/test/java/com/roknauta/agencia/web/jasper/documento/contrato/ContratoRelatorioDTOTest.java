package com.roknauta.agencia.web.jasper.documento.contrato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.DadosAgenciaContratoDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.web.jasper.DadosRelatoriosTeste;

class ContratoRelatorioDTOTest {

    @Test
    void deveMapearContratoCompletoAgenciaOrcamentoServicosEClausulas() throws Exception {
        ContratoDTO contrato = new ContratoDTO();
        DadosRelatoriosTeste.preencherDocumento(contrato);
        contrato.setDataAssinatura(Instant.parse("2026-10-09T18:30:00Z"));

        OrcamentoDTO orcamento = DadosRelatoriosTeste.orcamentoCompleto();
        orcamento.setNumero("ORC-2026-001");
        contrato.setOrcamento(orcamento);

        DadosAgenciaContratoDTO agencia = new DadosAgenciaContratoDTO();
        agencia.setAgenciaRazaoSocial("Agência de Viagens Exemplo Ltda.");
        agencia.setAgenciaNomeFantasia("Viagens Exemplo");
        agencia.setAgenciaCnpj("12.345.678/0001-90");
        agencia.setAgenciaEndereco("Av. Central, 500, Belo Horizonte/MG");
        agencia.setAgenciaEmail("contato@exemplo.com.br");
        agencia.setAgenciaTelefone("(31) 3333-4444");
        agencia.setAgenciaRepresentante("Maria Representante");
        agencia.setAgenciaCadastur("123456789");
        agencia.setAgenciaHorario("Segunda a sexta, das 9h às 18h");
        agencia.setAgenciaUrgencia("Plantão pelo telefone (31) 99999-8888");
        contrato.setDadosAgencia(agencia);

        ContratoRelatorioDTO relatorio = new ContratoRelatorioDTO(contrato);

        assertEquals("DOC-TESTE-2027-001", relatorio.getNumero());
        assertEquals("ORC-2026-001", relatorio.getNumeroOrcamento());
        assertEquals("09/10/2026 15:30", relatorio.getDataAssinatura());
        assertEquals("Agência de Viagens Exemplo Ltda.", relatorio.getRazaoSocialAgencia());
        assertEquals("Viagens Exemplo", relatorio.getNomeFantasiaAgencia());
        assertEquals("12.345.678/0001-90", relatorio.getCnpjAgencia());
        assertEquals("Av. Central, 500, Belo Horizonte/MG", relatorio.getEnderecoAgencia());
        assertEquals("contato@exemplo.com.br", relatorio.getEmailAgencia());
        assertEquals("(31) 3333-4444", relatorio.getTelefoneAgencia());
        assertEquals("Maria Representante", relatorio.getRepresentanteAgencia());
        assertEquals("123456789", relatorio.getCadasturAgencia());
        assertEquals("Segunda a sexta, das 9h às 18h", relatorio.getHorarioAgencia());
        assertEquals("Plantão pelo telefone (31) 99999-8888", relatorio.getUrgenciaAgencia());
        assertEquals(2, relatorio.getTrajetosAereos().size());
        assertEquals(1, relatorio.getTrajetosRodoviarios().size());
        assertEquals(1, relatorio.getHospedagens().size());
        assertEquals(2, relatorio.getTransfers().size());
        assertEquals(2, relatorio.getAlugueis().size());
        assertEquals(1, relatorio.getSegurosViagem().size());
        assertEquals(1, relatorio.getESims().size());
        assertFalse(relatorio.getClausulas().isEmpty());
        assertNotNull(relatorio.getClausulas().getFirst().getTexto());
        assertTrue(relatorio.getClausulas().stream().anyMatch(clausula -> clausula.getTexto().contains("Anexo I")));
    }
}
