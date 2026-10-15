package com.roknauta.agencia.web.jasper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.sf.jasperreports.engine.JasperReport;

class ReportTemplatesCompileTest {

    @Test
    void deveCompilarTemplatesDeRelatorios() throws Exception {
        List<String> templates = List.of("orcamento.jrxml", "emissao.jrxml", "contrato.jrxml", "contrato-blocos.jrxml",
            "servicos/servico-hospedagens.jrxml", "servicos/servico-transfer.jrxml",
            "servicos/servico-trajeto-rodoviario.jrxml", "servicos/servico-trajeto-aereo.jrxml",
            "servicos/servico-seguro-viagem.jrxml", "servicos/servico-e-sim.jrxml",
            "servicos/servico-aluguel-veiculo.jrxml");

        for (String template : templates) {
            JasperReport report = JasperUtils.compilar(template);
            assertNotNull(report, template);
        }
    }
}
