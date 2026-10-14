package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.dto.documento.ESimDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.service.documento.ESimService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("eSimController")
@ViewScoped
public class ESimController extends CrudControllerBase<ESimDTO, ESimService> {

    private final OrcamentoService orcamentoService;
    private Long idOrcamento;
    private OrcamentoDTO orcamentoSelecionado;

    public ESimController(ESimService service, OrcamentoService orcamentoService) {
        super(service);
        this.orcamentoService = orcamentoService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("e.sim");
    }

    @Override
    public void novo() {
        super.novo();
        editForm = new ESimDTO();
        if (idOrcamento != null) {
            orcamentoSelecionado = orcamentoService.findById(idOrcamento);
        }
    }

    @Override
    public void edit(Long id) {
        editForm = service.findById(id);
        idOrcamento = localizarOrcamento(id);
        orcamentoSelecionado = orcamentoService.findById(idOrcamento);
        editing = true;
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public void selecionarOrcamento() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
        atualizarResultados();
    }

    public List<ESimDTO> getESimsDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    @Override
    public void save() {
        if (idOrcamento == null) {
            throw new IllegalArgumentException("Selecione um orçamento.");
        }
        if (editForm.getId() != null) {
            super.save();
            return;
        }
        service.salvarNoOrcamento(idOrcamento, editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    protected void atualizarResultados() {
        setResult(getESimsDoOrcamento());
        pesquisaRealizada = idOrcamento != null;
    }

    @Override
    public void delete(Long id) {
        if (idOrcamento != null) {
            service.excluirDoOrcamento(idOrcamento, id);
            atualizarResultados();
        }
    }

    public void editarESim(Long id) {
        edit(id);
    }

    public void selecionarOrcamentoNoForm() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
    }


    @Override
    public void voltarPesquisa() {
        super.voltarPesquisa();
        orcamentoSelecionado = null;
        idOrcamento = null;
    }

    public void adicionarCodigo() {
        editForm.getCodigosAtivacao().add("");
    }

    public void removerCodigo(int indice) {
        if (indice >= 0 && indice < editForm.getCodigosAtivacao().size()) {
            editForm.getCodigosAtivacao().remove(indice);
        }
    }

    public Long getIdOrcamento() {
        return idOrcamento;
    }

    public void setIdOrcamento(Long idOrcamento) {
        this.idOrcamento = idOrcamento;
    }

    public OrcamentoDTO getOrcamentoSelecionado() {
        return orcamentoSelecionado;
    }

    public void setOrcamentoSelecionado(OrcamentoDTO orcamentoSelecionado) {
        this.orcamentoSelecionado = orcamentoSelecionado;
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
    }

    private Long localizarOrcamento(Long idESim) {
        return service.findIdByESim(idESim);
    }
}
