package com.roknauta.agencia.web.crud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.documento.ContratoService;
import com.roknauta.agencia.service.documento.EmissaoService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Component;

@Component("emissaoController")
@ViewScoped
public class EmissaoController extends CrudControllerBase<EmissaoDTO, EmissaoService> {

    private final ContratoService contratoService;
    private ContratoDTO contratoSelecionado;
    private BilheteAereoDTO bilheteForm = new BilheteAereoDTO();
    private Integer indiceViajanteBilhete;
    private Integer indiceTrechoBilhete;

    public EmissaoController(EmissaoService service, ContratoService contratoService) {
        super(service);
        this.contratoService = contratoService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("emissao");
    }

    @Override
    public void novo() {
        super.novo();
        contratoSelecionado = null;
        bilheteForm = new BilheteAereoDTO();
        indiceViajanteBilhete = null;
        indiceTrechoBilhete = null;
    }

    @Override
    public void edit(Long id) {
        super.edit(id);
        contratoSelecionado = editForm.getContrato();
        bilheteForm = new BilheteAereoDTO();
    }

    @Override
    public void save() {
        if (editForm.getContrato() == null || editForm.getContrato().getId() == null) {
            addErrorMessage("emissao.contrato.obrigatorio");
            return;
        }
        editForm = service.salvar(editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    public List<ContratoDTO> completeContrato(String texto) {
        return contratoService.buscarDisponiveisParaEmissao(texto);
    }

    public void selecionarContrato() {
        if (contratoSelecionado == null || contratoSelecionado.getId() == null) {
            return;
        }
        editForm = service.prepararPorContrato(contratoSelecionado.getId());
        contratoSelecionado = editForm.getContrato();
    }

    public void novoBilhete() {
        bilheteForm = new BilheteAereoDTO();
        indiceViajanteBilhete = null;
        indiceTrechoBilhete = null;
    }

    public void adicionarBilhete() {
        if (indiceViajanteBilhete == null || indiceTrechoBilhete == null) {
            throw new IllegalArgumentException("Selecione o viajante e o trecho aéreo do bilhete.");
        }
        List<ViajanteDTO> viajantes = editForm.getViajantes();
        List<TrechoAereoDTO> trechos = getTrechosDisponiveis();
        if (indiceViajanteBilhete < 0 || indiceViajanteBilhete >= viajantes.size() || indiceTrechoBilhete < 0
            || indiceTrechoBilhete >= trechos.size()) {
            throw new IllegalArgumentException("Viajante ou trecho aéreo inválido.");
        }
        bilheteForm.setViajante(viajantes.get(indiceViajanteBilhete));
        bilheteForm.setTrechoAereo(trechos.get(indiceTrechoBilhete));
        editForm.getBilhetesAereos().add(bilheteForm);
        novoBilhete();
    }

    public void removerBilhete(int indice) {
        if (indice >= 0 && indice < editForm.getBilhetesAereos().size()) {
            editForm.getBilhetesAereos().remove(indice);
        }
    }

    public List<SelectItem> getViajantesOptions() {
        List<SelectItem> opcoes = new ArrayList<>();
        if (editForm != null && editForm.getViajantes() != null) {
            for (int i = 0; i < editForm.getViajantes().size(); i++) {
                ViajanteDTO viajante = editForm.getViajantes().get(i);
                opcoes.add(new SelectItem(i, viajante.getNome()));
            }
        }
        return opcoes;
    }

    public List<SelectItem> getTrechosOptions() {
        List<SelectItem> opcoes = new ArrayList<>();
        List<TrechoAereoDTO> trechos = getTrechosDisponiveis();
        for (int i = 0; i < trechos.size(); i++) {
            TrechoAereoDTO trecho = trechos.get(i);
            String numero = trecho.getVoo() == null ? "Voo" : trecho.getVoo().getNumero();
            opcoes.add(new SelectItem(i, numero + " - " + trecho.getDataPartida() + " → " + trecho.getDataChegada()));
        }
        return opcoes;
    }

    private List<TrechoAereoDTO> getTrechosDisponiveis() {
        if (editForm == null || editForm.getTrajetosAereos() == null) {
            return List.of();
        }
        return editForm.getTrajetosAereos().stream().filter(trajeto -> trajeto != null && trajeto.getTrechos() != null)
            .flatMap(trajeto -> trajeto.getTrechos().stream()).toList();
    }

    public void gerarPdf(Long id) throws IOException, JRException {
        byte[] pdf = service.gerarPdf(id);
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().responseReset();
        context.getExternalContext().setResponseContentType("application/pdf");
        context.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=emissao.pdf");
        context.getExternalContext().setResponseContentLength(pdf.length);
        context.getExternalContext().getResponseOutputStream().write(pdf);
        context.responseComplete();
    }

    public ContratoDTO getContratoSelecionado() {
        return contratoSelecionado;
    }

    public void setContratoSelecionado(ContratoDTO contratoSelecionado) {
        this.contratoSelecionado = contratoSelecionado;
    }

    public BilheteAereoDTO getBilheteForm() {
        return bilheteForm;
    }

    public void setBilheteForm(BilheteAereoDTO bilheteForm) {
        this.bilheteForm = bilheteForm;
    }

    public Integer getIndiceViajanteBilhete() {
        return indiceViajanteBilhete;
    }

    public void setIndiceViajanteBilhete(Integer indiceViajanteBilhete) {
        this.indiceViajanteBilhete = indiceViajanteBilhete;
    }

    public Integer getIndiceTrechoBilhete() {
        return indiceTrechoBilhete;
    }

    public void setIndiceTrechoBilhete(Integer indiceTrechoBilhete) {
        this.indiceTrechoBilhete = indiceTrechoBilhete;
    }
}
