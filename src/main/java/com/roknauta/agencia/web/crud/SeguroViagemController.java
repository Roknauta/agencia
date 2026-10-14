package com.roknauta.agencia.web.crud;

import java.util.List;
import java.util.ArrayList;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.service.documento.SeguroViagemService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("seguroViagemController")
@ViewScoped
public class SeguroViagemController extends CrudControllerBase<SeguroViagemDTO, SeguroViagemService> {

    private final OrcamentoService orcamentoService;
    private Long idOrcamento;
    private OrcamentoDTO orcamentoSelecionado;

    public SeguroViagemController(SeguroViagemService service, OrcamentoService orcamentoService) {
        super(service);
        this.orcamentoService = orcamentoService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("seguro.viagem");
    }

    @Override
    public void novo() {
        super.novo();
        if (idOrcamento == null) {
            orcamentoSelecionado = null;
        } else {
            orcamentoSelecionado = orcamentoService.findById(idOrcamento);
        }
    }

    @Override
    public void edit(Long id) {
        editForm = service.findById(id);
        idOrcamento = localizarOrcamento(id);
        orcamentoSelecionado = idOrcamento == null ? null : orcamentoService.findById(idOrcamento);
        editing = true;
    }

    @Override
    public void save() {
        if (editForm.getId() != null) {
            super.save();
            return;
        }
        if (idOrcamento == null) {
            throw new IllegalArgumentException("Selecione um orçamento.");
        }
        service.salvarNoOrcamento(idOrcamento, editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public void selecionarOrcamento() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
        if (idOrcamento != null && editing && editForm != null) {
            List<ViajanteDTO> viajantes = orcamentoService.findById(idOrcamento).getViajantes();
            if (editForm != null) {
                if (editForm.getViajantesCobertos() == null) {
                    editForm.setViajantesCobertos(new ArrayList<>());
                }
                editForm.getViajantesCobertos()
                    .removeIf(viajante -> viajantes.stream().noneMatch(item -> item.getId().equals(viajante.getId())));
            }
        }
        atualizarResultados();
    }

    public List<ViajanteDTO> completeViajante(String texto) {
        if (idOrcamento == null) {
            return List.of();
        }
        return orcamentoService.findViajantes(idOrcamento, texto);
    }

    public List<SeguroViagemDTO> getSegurosDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    @Override
    protected void atualizarResultados() {
        setResult(getSegurosDoOrcamento());
        pesquisaRealizada = idOrcamento != null;
    }

    @Override
    public void delete(Long id) {
        if (idOrcamento != null) {
            service.excluirDoOrcamento(idOrcamento, id);
            atualizarResultados();
        }
    }

    public void editarSeguro(Long id) {
        edit(id);
    }

    public List<ViajanteDTO> getViajantesDoOrcamento() {
        return idOrcamento == null ? List.of() : orcamentoService.findById(idOrcamento).getViajantes();
    }

    @Override
    public void voltarPesquisa() {
        super.voltarPesquisa();
        orcamentoSelecionado = null;
        idOrcamento = null;
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

    private Long localizarOrcamento(Long idSeguro) {
        return service.findIdBySeguroViagem(idSeguro);
    }
}
