package com.roknauta.agencia.web.jasper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.sf.jasperreports.engine.JasperReport;

class ReportTemplatesCompileTest {

    @Test
    void deveCompilarTemplatesDeRelatorios() throws Exception {
        List<String> templates = List.of(
                "orcamento-passagem-hospedagem-JBD.jrxml",
                "emissao-passagem-JBD.jrxml",
                "contrato-JBD.jrxml",
                "contrato-blocos-JBD.jrxml",
                "hospedagens-JBD.jrxml",
                "orcamento-servicos-JBD.jrxml",
                "tarifa-aerea-regras-JBD.jrxml");

        for (String template : templates) {
            JasperReport report = JasperUtils.compilar(template);
            assertNotNull(report, template);
        }
    }
}
