package com.roknauta.agencia.web.jasper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.dto.CidadeDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.web.jasper.documento.contrato.ContratoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.emissao.EmissaoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.orcamento.OrcamentoRelatorioDTO;

import net.sf.jasperreports.engine.JasperReport;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

class ReportPdfSamplesTest {

    @Test
    void deveGerarCopiasPdfDeTesteDosTresRelatorios() throws Exception {
        Path diretorio = Path.of("target", "test-relatorios");
        Files.createDirectories(diretorio);

        Image logo;
        try (InputStream recurso = JasperUtils.abrirRecurso("logo-jbd.png")) {
            logo = ImageIO.read(recurso);
        }
        JasperReport relatorioHospedagens = JasperUtils.compilar("hospedagens-JBD.jrxml");
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO", logo);
        parametros.put("RELATORIO_HOSPEDAGENS", relatorioHospedagens);

        OrcamentoDTO orcamento = DadosRelatoriosTeste.orcamentoCompleto();
        byte[] pdfOrcamento = JasperUtils.gerarPdf("orcamento-passagem-hospedagem-JBD.jrxml",
            new OrcamentoRelatorioDTO(orcamento), parametros);
        gravar(diretorio.resolve("orcamento-teste.pdf"), pdfOrcamento);

        EmissaoDTO emissao = new EmissaoDTO();
        DadosRelatoriosTeste.preencherDocumento(emissao);
        emissao.setDataDocumento(LocalDate.of(2026, 10, 9));
        ContratoDTO contratoEmissao = new ContratoDTO();
        contratoEmissao.setNumero("CTR-2026-001");
        emissao.setContrato(contratoEmissao);
        BilheteAereoDTO bilheteIda = new BilheteAereoDTO();
        bilheteIda.setViajante(emissao.getViajantes().getFirst());
        bilheteIda.setTrechoAereo(DadosRelatoriosTeste.trecho("JJ1234", "CNF", "GRU", LocalTime.of(8, 0),
            LocalTime.of(9, 15), LocalDate.of(2027, 2, 10)));
        bilheteIda.setNumeroBilhete("9571234567890");
        bilheteIda.setAssento("12A");
        BilheteAereoDTO bilheteVolta = new BilheteAereoDTO();
        bilheteVolta.setViajante(emissao.getViajantes().get(1));
        bilheteVolta.setTrechoAereo(DadosRelatoriosTeste.trecho("TP1020", "LIS", "CNF", LocalTime.of(10, 0),
            LocalTime.of(18, 20), LocalDate.of(2027, 2, 18)));
        bilheteVolta.setNumeroBilhete("0479876543210");
        bilheteVolta.setAssento("14C");
        emissao.setBilhetesAereos(java.util.List.of(bilheteIda, bilheteVolta));
        byte[] pdfEmissao =
            JasperUtils.gerarPdf("emissao-passagem-JBD.jrxml", new EmissaoRelatorioDTO(emissao), parametros);
        gravar(diretorio.resolve("emissao-teste.pdf"), pdfEmissao);

        ContratoDTO contrato = new ContratoDTO();
        DadosRelatoriosTeste.preencherDocumento(contrato);
        contrato.setOrcamento(orcamento);
        contrato.setDataConsultaPrecos(java.time.Instant.parse("2026-10-09T17:00:00Z"));
        contrato.setReferenciaPreReserva("PR-123456");
        contrato.setPreReservaValidaAte(java.time.Instant.parse("2026-10-10T18:00:00Z"));
        AgenciaDTO agencia = new AgenciaDTO();
        agencia.setRazaoSocial("Agência de Teste Ltda.");
        agencia.setCnpj("12.345.678/0001-90");
        agencia.setEmail("teste@agencia.com.br");
        CidadeDTO cidadeAgencia = new CidadeDTO();
        cidadeAgencia.setFusoHorario("America/Sao_Paulo");
        agencia.setCidade(cidadeAgencia);
        byte[] pdfContrato = JasperUtils.gerarContratoPdf(new ContratoRelatorioDTO(contrato, agencia), Map.of());
        gravar(diretorio.resolve("contrato-teste.pdf"), pdfContrato);

        assertTrue(pdfOrcamento.length > 1000);
        assertTrue(pdfEmissao.length > 1000);
        assertTrue(pdfContrato.length > 1000);
        String textoOrcamento = textoPdf(pdfOrcamento);
        String textoOrcamentoNormalizado = textoOrcamento.replaceAll("\\s+", "");
        String textoContratoNormalizado = textoPdf(pdfContrato).replace("\u00a0", "").replaceAll("\\s+", "");
        String textoEmissao = textoPdf(pdfEmissao).replaceAll("\\s+", "");
        assertTrue(textoOrcamentoNormalizado.contains("JJ1234"), textoOrcamento);
        assertTrue(textoOrcamentoNormalizado.contains("JJ5678"), textoOrcamento);
        assertTrue(textoOrcamentoNormalizado.contains("TP1020"), textoOrcamento);
        assertTrue(textoContratoNormalizado.contains("Passagensaéreas"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("PR-123456"), textoContratoNormalizado);
        assertTrue(textoEmissao.contains("CTR-2026-001"), textoEmissao);
        assertTrue(textoContratoNormalizado.contains("ViaçãoExemplo"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("HotelCentralLisboa"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("TransferExemplo"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("Alugueldeveículo"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("SeguradoraExemplo"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("eSIM"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("Valor total dos serviços".replaceAll("\\s+", "")),
            textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("R$7.955,00"), textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("regrasespecíficasaplicáveisaotransporteeaocasoconcreto."),
            textoContratoNormalizado);
        assertTrue(textoContratoNormalizado.contains("Outrosserviçosrodoviáriosobservarãosuaregulamentaçãoprópria."),
            textoContratoNormalizado);
    }

    private static void gravar(Path caminho, byte[] conteudo) throws Exception {
        Files.write(caminho, conteudo);
    }

    private static String textoPdf(byte[] conteudo) throws Exception {
        PdfReader reader = new PdfReader(conteudo);
        StringBuilder texto = new StringBuilder();
        PdfTextExtractor extrator = new PdfTextExtractor(reader);
        for (int pagina = 1; pagina <= reader.getNumberOfPages(); pagina++) {
            texto.append(extrator.getTextFromPage(pagina));
        }
        reader.close();
        return texto.toString();
    }
}
