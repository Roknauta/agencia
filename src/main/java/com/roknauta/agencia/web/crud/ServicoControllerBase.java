package com.roknauta.agencia.web.crud;

import java.math.BigDecimal;
import java.util.List;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

/** Comportamento comum aos cadastros de serviços associados a um orçamento. */
@ViewScoped
public abstract class ServicoControllerBase<T extends ServicoDTO, S extends CrudService<T>>
    extends CrudControllerBase<T, S> {

    protected final OrcamentoService orcamentoService;

    @Getter
    @Setter
    protected Long idOrcamento;

    @Getter
    @Setter
    protected OrcamentoDTO orcamentoSelecionado;

    private int indiceTaxaEmEdicao = -1;

    protected ServicoControllerBase(S service, OrcamentoService orcamentoService) {
        super(service);
        this.orcamentoService = orcamentoService;
        taxaForm = new TaxaDTO();
    }

    @Override
    public void novo() {
        super.novo();
        taxaForm = new TaxaDTO();
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public void selecionarOrcamento() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
        aoSelecionarOrcamento();
        atualizarResultados();
    }

    protected void aoSelecionarOrcamento() {}

    protected Long localizarOrcamento(Long idServico) {
        return null;
    }

    protected abstract List<T> buscarServicosDoOrcamento(Long idOrcamento);

    protected abstract T salvarNoOrcamento(Long idOrcamento, T servico);

    protected abstract void excluirDoOrcamento(Long idOrcamento, Long idServico);

    @Override
    public void edit(Long id) {
        super.edit(id);
        taxaForm = new TaxaDTO();
        idOrcamento = localizarOrcamento(id);
        orcamentoSelecionado = idOrcamento == null ? null : orcamentoService.findById(idOrcamento);
    }

    @Override
    public void save() {
        if (idOrcamento == null) {
            throw new IllegalArgumentException("Selecione um orçamento.");
        }
        editForm = salvarNoOrcamento(idOrcamento, editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    public void delete(Long id) {
        if (idOrcamento != null) {
            excluirDoOrcamento(idOrcamento, id);
            atualizarResultados();
        }
    }

    @Override
    protected void atualizarResultados() {
        setResult(idOrcamento == null ? List.of() : buscarServicosDoOrcamento(idOrcamento));
        pesquisaRealizada = idOrcamento != null;
    }

    @Override
    public void voltarPesquisa() {
        super.voltarPesquisa();
        orcamentoSelecionado = null;
        idOrcamento = null;
    }

    public void novaTaxa() {
        taxaForm = new TaxaDTO();
        if (editForm != null) {
            taxaForm.setMoeda(editForm.getMoeda());
        }
        indiceTaxaEmEdicao = -1;
    }

    public void editarTaxa(int indice) {
        if (editForm == null || indice < 0 || indice >= editForm.getTaxas().size()) {
            return;
        }
        TaxaDTO taxa = editForm.getTaxas().get(indice);
        taxaForm = new TaxaDTO();
        taxaForm.setId(taxa.getId());
        taxaForm.setTipoTaxa(taxa.getTipoTaxa());
        taxaForm.setValor(taxa.getValor() == null ? BigDecimal.ZERO : taxa.getValor());
        taxaForm.setMoeda(taxa.getMoeda());
        indiceTaxaEmEdicao = indice;
    }

    public void confirmarTaxa() {
        if (editForm == null || taxaForm == null) {
            return;
        }
        if (indiceTaxaEmEdicao >= 0 && indiceTaxaEmEdicao < editForm.getTaxas().size()) {
            editForm.getTaxas().set(indiceTaxaEmEdicao, taxaForm);
        } else {
            editForm.getTaxas().add(taxaForm);
        }
        taxaForm = new TaxaDTO();
        indiceTaxaEmEdicao = -1;
    }

    public void cancelarTaxa() {
        taxaForm = new TaxaDTO();
        indiceTaxaEmEdicao = -1;
    }

    public void removerTaxa(int indice) {
        if (editForm != null && indice >= 0 && indice < editForm.getTaxas().size()) {
            editForm.getTaxas().remove(indice);
        }
    }

    public Moeda[] getMoedas() {
        return Moeda.values();
    }
}
