package com.roknauta.agencia.web.crud;

import java.io.Serializable;
import java.util.List;
import java.math.BigDecimal;

import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.web.ControllerBase;
import com.roknauta.agencia.web.Msg;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.GenericTypeResolver;


public abstract class CrudControllerBase<T extends BaseDTO, S extends CrudService<T>> extends ControllerBase
    implements Serializable {

    @Getter
    @Setter
    protected T editForm;
    @Getter
    @Setter
    protected T searchForm;
    @Getter
    @Setter
    protected TaxaDTO taxaForm;
    private int indiceTaxaEmEdicao = -1;
    @Getter
    @Setter
    private List<T> result = List.of();
    @Getter
    protected boolean editing;
    @Getter
    protected boolean pesquisaRealizada;

    protected final S service;
    private Class<T> dtoClass;

    public abstract String pageTitle();

    @SuppressWarnings("unchecked")
    protected CrudControllerBase(S service) {
        this.service = service;
        setDtoClass();
    }

    @SuppressWarnings("unchecked")
    private void setDtoClass() {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), CrudControllerBase.class);

        if (typeArguments == null) {
            throw new IllegalStateException(Msg.get("crud.tipo.formulario.nao.resolvido"));
        }
        dtoClass = (Class<T>) typeArguments[0];
    }

    protected T newForm() {
        try {
            return dtoClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(Msg.get("crud.formulario.erro.instanciar"), e);
        }
    }

    @PostConstruct
    public void inicializar() {
        searchForm = newForm();
    }

    public void novo() {
        editForm = newForm();
        editing = true;
    }

    public void voltarPesquisa() {
        editForm = null;
        editing = false;
    }

    public void edit(Long id) {
        editForm = service.findById(id);
        editing = true;
    }

    public void save() {
        editForm = service.save(editForm);
        aposGravar();
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    public void delete(Long id) {
        service.delete(id);
        finalizarExclusao(id);
    }

    public Moeda[] getMoedas() {
        return Moeda.values();
    }

    public void novaTaxa() {
        taxaForm = new TaxaDTO();
        if (editForm instanceof ServicoDTO servico) {
            taxaForm.setMoeda(servico.getMoeda());
        }
        indiceTaxaEmEdicao = -1;
    }

    public void editarTaxa(int indice) {
        if (!(editForm instanceof ServicoDTO servico) || indice < 0 || indice >= servico.getTaxas().size()) {
            return;
        }
        TaxaDTO taxa = servico.getTaxas().get(indice);
        taxaForm = new TaxaDTO();
        taxaForm.setId(taxa.getId());
        taxaForm.setTipoTaxa(taxa.getTipoTaxa());
        taxaForm.setValor(taxa.getValor() == null ? BigDecimal.ZERO : taxa.getValor());
        taxaForm.setMoeda(taxa.getMoeda());
        indiceTaxaEmEdicao = indice;
    }

    public void confirmarTaxa() {
        if (!(editForm instanceof ServicoDTO servico) || taxaForm == null) {
            return;
        }
        if (indiceTaxaEmEdicao >= 0 && indiceTaxaEmEdicao < servico.getTaxas().size()) {
            servico.getTaxas().set(indiceTaxaEmEdicao, taxaForm);
        } else {
            servico.getTaxas().add(taxaForm);
        }
        taxaForm = null;
        indiceTaxaEmEdicao = -1;
    }

    public void removerTaxa(int indice) {
        if (editForm instanceof ServicoDTO servico && indice >= 0 && indice < servico.getTaxas().size()) {
            servico.getTaxas().remove(indice);
        }
    }

    private void finalizarExclusao(Long id) {
        if (editForm != null && id.equals(editForm.getId())) {
            voltarPesquisa();
        }
        atualizarResultados();
        addInfoMessage("crud.registro.excluido");
    }

    public void pesquisar() {
        atualizarResultados();
    }

    protected void atualizarResultados() {
        result = service.find(searchForm);
        pesquisaRealizada = true;
    }

    protected void aposGravar() {}
}
