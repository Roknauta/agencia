package com.roknauta.agencia.web.jasper.documento.contrato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.dto.CidadeDTO;
import com.roknauta.agencia.web.jasper.DadosRelatoriosTeste;

class ContratoRelatorioDTOTest {

    @Test
    void deveMapearContratoCompletoAgenciaOrcamentoServicosEClausulas() throws Exception {
        ContratoDTO contrato = new ContratoDTO();
        DadosRelatoriosTeste.preencherDocumento(contrato);
        contrato.setDataAssinatura(Instant.parse("2026-10-09T18:30:00Z"));
        contrato.setDataConsultaPrecos(Instant.parse("2026-10-09T17:00:00Z"));
        contrato.setReferenciaPreReserva("PR-123456");
        contrato.setPreReservaValidaAte(Instant.parse("2026-10-10T18:00:00Z"));

        OrcamentoDTO orcamento = DadosRelatoriosTeste.orcamentoCompleto();
        orcamento.setNumero("ORC-2026-001");
        contrato.setOrcamento(orcamento);

        AgenciaDTO agencia = new AgenciaDTO();
        agencia.setRazaoSocial("Agência de Viagens Exemplo Ltda.");
        agencia.setNomeFantasia("Viagens Exemplo");
        agencia.setCnpj("12.345.678/0001-90");
        agencia.setEndereco("Av. Central, 500, Belo Horizonte/MG");
        agencia.setEmail("contato@exemplo.com.br");
        agencia.setTelefone("(31) 3333-4444");
        agencia.setRepresentante("Maria Representante");
        agencia.setCadastur("123456789");
        agencia.setHorario("Segunda a sexta, das 9h às 18h");
        agencia.setUrgencia("Plantão pelo telefone (31) 99999-8888");
        CidadeDTO cidade = new CidadeDTO();
        cidade.setFusoHorario("America/New_York");
        agencia.setCidade(cidade);

        ContratoRelatorioDTO relatorio = new ContratoRelatorioDTO(contrato, agencia);

        assertEquals("DOC-TESTE-2027-001", relatorio.getNumero());
        assertEquals("ORC-2026-001", relatorio.getNumeroOrcamento());
        assertEquals("09/10/2026 14:30", relatorio.getDataAssinatura());
        assertEquals("09/10/2026 13:00", relatorio.getDataConsultaPrecos());
        assertEquals("PR-123456", relatorio.getReferenciaPreReserva());
        assertEquals("10/10/2026 14:00", relatorio.getPreReservaValidaAte());
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
        assertTrue(relatorio.getConteudoContrato().stream()
            .anyMatch(bloco -> bloco.getTitulo().equals("Consulta de preços e pré-reserva")
                && bloco.getTexto().contains("PR-123456")));
        assertTrue(relatorio.getConteudoContrato().stream()
            .anyMatch(bloco -> bloco.getTitulo().equals("Viajantes") && bloco.getTexto().contains("Nome: Joana Exemplo")
                && bloco.getTexto().contains("Tipo de documento: CPF")
                && bloco.getTexto().contains("Documento: 12345678900")
                && bloco.getTexto().contains("Data de nascimento: 14/03/1988")));
    }
}
