package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.domain.TipoBagagem;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.dto.documento.*;
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
import org.springframework.stereotype.Component;

import java.util.List;

@Component("trajetoAereoController")
@ViewScoped
public class TrajetoAereoController extends ServicoControllerBase<TrajetoAereoDTO, TrajetoAereoService> {

    private final VooService vooService;
    @Getter
    @Setter
    private CompanhiaAereaDTO companhiaAerea;
    @Getter
    @Setter
    private TrechoAereoDTO trechoForm;
    @Getter
    @Setter
    private BagagemTrajetoAereoDTO bagagemForm;
    private int indiceBagagem = -1;

    public TrajetoAereoController(TrajetoAereoService service, OrcamentoService orcamentoService,
        VooService vooService) {
        super(service, orcamentoService);
        this.vooService = vooService;
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
        bagagemForm = null;
        indiceBagagem = -1;
    }

    @Override
    public void edit(Long id) {
        super.edit(id);
        trechoForm = new TrechoAereoDTO();
        bagagemForm = null;
        indiceBagagem = -1;
        // Edição de trajetos é iniciada a partir do orçamento selecionado.
    }

    public List<VooDTO> completeVoo(String texto) {
        return companhiaAerea == null ? List.of()
            : vooService.findByNumeroCompanhiaAerea(texto, companhiaAerea.getId());
    }

    public SentidoVoo[] getSentidos() {
        return SentidoVoo.values();
    }

    public CabineAerea[] getCabines() {
        return CabineAerea.values();
    }

    public List<TrajetoAereoDTO> getTrajetosDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    public TipoBagagem[] getTiposBagagem() {
        return TipoBagagem.values();
    }

    public void novoTrecho() {
        trechoForm = new TrechoAereoDTO();
        PrimeFaces.current().executeScript("PF('trechoDialogWidget').show();");
    }

    public void companhiaAereaEvent(SelectEvent<CompanhiaAereaDTO> event) {
        this.companhiaAerea = event.getObject();
    }

    public void confirmarTrecho() {
        if (trechoForm != null) {
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
        companhiaAerea = trechoForm.getVoo() == null ? null : trechoForm.getVoo().getCompanhiaAerea();
    }

    public void novaBagagem() {
        bagagemForm = new BagagemTrajetoAereoDTO();
        indiceBagagem = -1;
        PrimeFaces.current().executeScript("PF('bagagemDialogWidget').show();");
    }

    public void editarBagagem(int indice) {
        bagagemForm = editForm.getBagagens().get(indice);
        indiceBagagem = indice;
        PrimeFaces.current().executeScript("PF('bagagemDialogWidget').show();");
    }

    public void confirmarBagagem() {
        if (bagagemForm == null || bagagemForm.getTipoBagagem() == null || bagagemForm.getQuantidade() < 1) {
            return;
        }
        if (indiceBagagem >= 0) {
            editForm.getBagagens().set(indiceBagagem, bagagemForm);
        } else {
            editForm.getBagagens().add(bagagemForm);
        }
        bagagemForm = null;
        indiceBagagem = -1;
        PrimeFaces.current().executeScript("PF('bagagemDialogWidget').hide();");
    }

    public void removerBagagem(int indice) {
        if (indice >= 0 && indice < editForm.getBagagens().size()) {
            editForm.getBagagens().remove(indice);
        }
    }

    public void removerTrecho(int indice) {
        editForm.getTrechos().remove(indice);
    }

    @Override
    protected List<TrajetoAereoDTO> buscarServicosDoOrcamento(Long idOrcamento) {
        return service.findByOrcamento(idOrcamento);
    }

    @Override
    protected Long localizarOrcamento(Long idServico) {
        return service.findIdByTrajetoAereo(idServico);
    }

    @Override
    protected TrajetoAereoDTO salvarNoOrcamento(Long idOrcamento, TrajetoAereoDTO servico) {
        return service.salvarNoOrcamento(idOrcamento, servico);
    }

    @Override
    protected void excluirDoOrcamento(Long idOrcamento, Long idServico) {
        service.excluirDoOrcamento(idOrcamento, idServico);
    }

    @Override
    protected void atualizarResultados() {
        setResult(getTrajetosDoOrcamento());
        pesquisaRealizada = CollectionUtils.isNotEmpty(getResult());
    }

}
