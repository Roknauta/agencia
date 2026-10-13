package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.dto.documento.*;
import com.roknauta.agencia.service.BagagemCompanhiaAereaService;
import com.roknauta.agencia.service.TarifaAereaService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.service.documento.TrajetoAereoService;
import com.roknauta.agencia.service.documento.VooService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ViewScoped
public class TrajetoAereoController extends CrudControllerBase<TrajetoAereoDTO, TrajetoAereoService> {

    private final OrcamentoService orcamentoService;
    private final VooService vooService;
    private final TarifaAereaService tarifaAereaService;
    @Getter
    @Setter
    private Long idOrcamento;
    @Getter
    @Setter
    private CompanhiaAereaDTO companhiaAerea;
    @Getter
    @Setter
    private OrcamentoDTO orcamentoSelecionado;
    @Getter
    @Setter
    private TrechoAereoDTO trechoForm;
    @Getter
    @Setter
    private List<BagagemTrechoAereoDTO> bagagensForm = new ArrayList<>();
    @Autowired
    private BagagemCompanhiaAereaService bagagemCompanhiaAereaService;

    public TrajetoAereoController(TrajetoAereoService service, OrcamentoService orcamentoService, VooService vooService,
        TarifaAereaService tarifaAereaService) {
        super(service);
        this.orcamentoService = orcamentoService;
        this.vooService = vooService;
        this.tarifaAereaService = tarifaAereaService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("trajeto.aereo");
    }

    @Override
    public void novo() {
        super.novo();
        orcamentoSelecionado = null;
        idOrcamento = null;
        trechoForm = new TrechoAereoDTO();
        bagagensForm = new ArrayList<>();
    }

    @Override
    public void edit(Long id) {
        super.edit(id);
        trechoForm = new TrechoAereoDTO();
        bagagensForm = new ArrayList<>();
        // Edição de trajetos é iniciada a partir do orçamento selecionado.
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public List<VooDTO> completeVoo(String texto) {
        return companhiaAerea == null ? List.of()
            : vooService.findByNumeroCompanhiaAerea(texto, companhiaAerea.getId());
    }

    public List<TarifaAereaDTO> completeTarifaAerea(String texto) {
        return companhiaAerea == null ? List.of()
            : tarifaAereaService.findByNomeCompanhiaAerea(texto, companhiaAerea.getId());
    }

    public SentidoVoo[] getSentidos() {
        return SentidoVoo.values();
    }

    public CabineAerea[] getCabines() {
        return CabineAerea.values();
    }

    public void selecionarOrcamento() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
    }

    public List<TrajetoAereoDTO> getTrajetosDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    public void novoTrecho() {
        trechoForm = new TrechoAereoDTO();
        PrimeFaces.current().executeScript("PF('trechoDialogWidget').show();");
    }

    public void companhiaAereaEvent(SelectEvent<CompanhiaAereaDTO> event) {
        this.companhiaAerea = event.getObject();
    }

    public void carregarBagagens() {
        bagagemCompanhiaAereaService.findByCompanhiaId(companhiaAerea.getId()).forEach(bagagem->{
            BagagemTrechoAereoDTO item = new BagagemTrechoAereoDTO();
            item.setBagagemCompanhiaAerea(bagagem);
            item.setQuantidade(0);
            bagagensForm.add(item);
        });
    }

    public void confirmarTrecho() {
        if (trechoForm != null) {
            trechoForm.setBagagens(bagagensForm.stream().filter(item -> item.getQuantidade() > 0).toList());
            int index = editForm.getTrechos().indexOf(trechoForm);
            if (index >= 0)
                editForm.getTrechos().set(index, trechoForm);
            else
                editForm.getTrechos().add(trechoForm);
            PrimeFaces.current().executeScript("PF('trechoDialogWidget').hide();");
        }
        trechoForm = null;
    }

    public void editarTrecho(int indice) {
        trechoForm = editForm.getTrechos().get(indice);
        bagagensForm = trechoForm.getBagagens() == null ? List.of() : trechoForm.getBagagens().stream().toList();
    }

    public void removerTrecho(int indice) {
        editForm.getTrechos().remove(indice);
    }

    @Override
    public void save() {
        if (idOrcamento == null)
            throw new IllegalArgumentException("Selecione um orçamento.");
        service.salvarNoOrcamento(idOrcamento, editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    protected void atualizarResultados() {
        setResult(getTrajetosDoOrcamento());
        pesquisaRealizada = CollectionUtils.isNotEmpty(getResult());
    }

    public void excluir(Long id) {
        if (idOrcamento == null)
            return;
        service.excluirDoOrcamento(idOrcamento, id);
        atualizarResultados();
    }

    @Override
    public void delete(Long id) {
        excluir(id);
    }
}
