package com.roknauta.agencia.web.crud;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.domain.documento.TipoDocumentoViajante;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
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
    private ViajanteDTO viajanteForm = new ViajanteDTO();

    public OrcamentoController(OrcamentoService service, CidadeService cidadeService) {
        super(service);
        this.cidadeService = cidadeService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("orcamento");
    }

    @Override
    public void novo() {
        super.novo();
        viajanteForm = new ViajanteDTO();
    }

    @Override
    public void edit(Long id) {
        super.edit(id);
        viajanteForm = new ViajanteDTO();
    }

    public TipoDocumentoViajante[] getTiposDocumentoViajante() {
        return TipoDocumentoViajante.values();
    }

    public void adicionarViajante() {
        if (viajanteForm == null || viajanteForm.getNome() == null || viajanteForm.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do viajante.");
        }
        editForm.getViajantes().add(viajanteForm);
        viajanteForm = new ViajanteDTO();
    }

    public void removerViajante(int indice) {
        if (indice >= 0 && indice < editForm.getViajantes().size()) {
            editForm.getViajantes().remove(indice);
        }
    }

    public ViajanteDTO getViajanteForm() {
        return viajanteForm;
    }

    public void setViajanteForm(ViajanteDTO viajanteForm) {
        this.viajanteForm = viajanteForm;
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
