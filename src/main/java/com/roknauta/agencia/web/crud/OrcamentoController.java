package com.roknauta.agencia.web.crud;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.service.CidadeService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.context.FacesContext;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class OrcamentoController extends CrudControllerBase<OrcamentoDTO, OrcamentoService> {

    private final CidadeService cidadeService;

    public OrcamentoController(OrcamentoService service, CidadeService cidadeService) {
        super(service);
        this.cidadeService = cidadeService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("orcamento");
    }

    public void gerarPdf(Long id) throws IOException, JRException {
        byte[] pdf = service.gerarPdf(id, Map.of());
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().responseReset();
        context.getExternalContext().setResponseContentType("application/pdf");
        context.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=orcamento.pdf");
        context.getExternalContext().setResponseContentLength(pdf.length);
        context.getExternalContext().getResponseOutputStream().write(pdf);
        context.responseComplete();
    }

    public StatusOrcamento[] getStatuses() {
        return StatusOrcamento.values();
    }

    public void registrarEnvio(Long id) {
        service.registrarEnvio(id);
        atualizarResultados();
    }

    public void registrarInteresseCliente(Long id) {
        service.registrarInteresseCliente(id);
        atualizarResultados();
    }
}
