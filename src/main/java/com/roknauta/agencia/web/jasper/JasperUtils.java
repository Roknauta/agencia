package com.roknauta.agencia.web.jasper;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import com.roknauta.agencia.web.jasper.documento.orcamento.OrcamentoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.contrato.ContratoRelatorioDTO;
import com.roknauta.agencia.web.jasper.documento.emissao.EmissaoRelatorioDTO;
import javax.imageio.ImageIO;

public final class JasperUtils {

    private static final String DIRETORIO_RELATORIOS = "/reports/";

    private JasperUtils() {}

    public static JasperReport compilar(String nomeArquivo) throws IOException, JRException {
        try (InputStream recurso = abrirRelatorio(nomeArquivo)) {
            return JasperCompileManager.compileReport(JRXmlLoader.load(recurso));
        }
    }

    public static byte[] gerarPdf(String nomeArquivo, Object dados, Map<String, Object> parametros)
        throws IOException, JRException {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do relatório são obrigatórios.");
        }
        return gerarPdf(nomeArquivo, new JRBeanCollectionDataSource(List.of(dados)), parametros);
    }

    public static byte[] gerarPdf(String nomeArquivo, List<?> dados, Map<String, Object> parametros)
        throws IOException, JRException {
        if (dados == null) {
            throw new IllegalArgumentException("Os dados do relatório são obrigatórios.");
        }
        return gerarPdf(nomeArquivo, new JRBeanCollectionDataSource(dados), parametros);
    }

    public static byte[] gerarPdf(String nomeArquivo, JRDataSource fonteDados, Map<String, Object> parametros)
        throws IOException, JRException {
        JasperReport relatorio = compilar(nomeArquivo);
        Map<String, Object> parametrosRelatorio = parametros == null ? new HashMap<>() : new HashMap<>(parametros);
        JasperPrint impressao = JasperFillManager.fillReport(relatorio, parametrosRelatorio, fonteDados);
        return JasperExportManager.exportReportToPdf(impressao);
    }

    public static byte[] gerarContratoPdf(ContratoRelatorioDTO contrato, Map<String, Object> parametros)
        throws IOException, JRException {
        if (contrato == null) {
            throw new IllegalArgumentException("Os dados do contrato são obrigatórios.");
        }
        Map<String, Object> parametrosContrato = parametros == null ? new HashMap<>() : new HashMap<>(parametros);
        parametrosContrato.putIfAbsent("RELATORIO_BLOCOS", compilar("contrato-blocos-JBD.jrxml"));
        return gerarPdf("contrato-JBD.jrxml", new JRBeanCollectionDataSource(List.of(contrato)), parametrosContrato);
    }

    public static byte[] gerarOrcamentoPdf(OrcamentoRelatorioDTO orcamento, Map<String, Object> parametros)
        throws IOException, JRException {
        if (orcamento == null) {
            throw new IllegalArgumentException("Os dados do orçamento são obrigatórios.");
        }
        Map<String, Object> parametrosOrcamento = parametros == null ? new HashMap<>() : new HashMap<>(parametros);
        parametrosOrcamento.putIfAbsent("RELATORIO_HOSPEDAGENS", compilar("hospedagens-JBD.jrxml"));
        if (!parametrosOrcamento.containsKey("LOGO")) {
            try (InputStream recurso = abrirRecurso("logo-jbd.png")) {
                Image logo = ImageIO.read(recurso);
                parametrosOrcamento.put("LOGO", logo);
            }
        }
        return gerarPdf("orcamento-passagem-hospedagem-JBD.jrxml", orcamento, parametrosOrcamento);
    }

    public static byte[] gerarEmissaoPdf(EmissaoRelatorioDTO emissao, Map<String, Object> parametros)
        throws IOException, JRException {
        if (emissao == null) {
            throw new IllegalArgumentException("Os dados da emissão são obrigatórios.");
        }
        Map<String, Object> parametrosEmissao = parametros == null ? new HashMap<>() : new HashMap<>(parametros);
        parametrosEmissao.putIfAbsent("RELATORIO_HOSPEDAGENS", compilar("hospedagens-JBD.jrxml"));
        if (!parametrosEmissao.containsKey("LOGO")) {
            try (InputStream recurso = abrirRecurso("logo-jbd.png")) {
                Image logo = ImageIO.read(recurso);
                parametrosEmissao.put("LOGO", logo);
            }
        }
        return gerarPdf("emissao-passagem-JBD.jrxml", emissao, parametrosEmissao);
    }

    public static InputStream abrirRecurso(String nomeArquivo) {
        String caminho = nomeArquivo.startsWith("/") ? nomeArquivo : DIRETORIO_RELATORIOS + nomeArquivo;
        InputStream recurso = JasperUtils.class.getResourceAsStream(caminho);
        if (recurso == null) {
            throw new IllegalArgumentException("Recurso Jasper não encontrado: " + caminho);
        }
        return recurso;
    }

    private static InputStream abrirRelatorio(String nomeArquivo) {
        return abrirRecurso(nomeArquivo);
    }
}
