package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/** Comportamento comum aos cadastros de serviços associados a um orçamento. */
@ViewScoped
public abstract class ServicoControllerBase<T extends ServicoDTO, S extends CrudService<T>>
    extends CrudControllerBase<T, S> {

    protected final OrcamentoService orcamentoService;

    @Getter
    @Setter
    protected TaxaDTO taxaForm;

    @Getter
    @Setter
    protected Long idOrcamento;

    @Getter
    @Setter
    protected OrcamentoDTO orcamento;

    protected ServicoControllerBase(S service, OrcamentoService orcamentoService) {
        super(service);
        this.orcamentoService = orcamentoService;
    }

    @Override
    public void novo() {
        super.novo();
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public void orcamentoSelect() {
        aoSelecionarOrcamento();
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
        orcamento = idOrcamento == null ? null : orcamentoService.findById(idOrcamento);
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
        orcamento = null;
        idOrcamento = null;
    }

    public void novaTaxa() {
        taxaForm = new TaxaDTO();
        taxaForm.setMoeda(editForm.getMoeda());
    }

    public void editarTaxa(TaxaDTO taxa) {
        this.taxaForm = taxa;
    }

    public void confirmarTaxa() {
        editForm.getTaxas().add(taxaForm);
        taxaForm = null;
    }

    public void cancelarTaxa() {
        taxaForm = null;
    }

    public void removerTaxa(TaxaDTO taxa) {
        editForm.getTaxas().remove(taxa);
    }

    public Moeda[] getMoedas() {
        return Moeda.values();
    }
}
