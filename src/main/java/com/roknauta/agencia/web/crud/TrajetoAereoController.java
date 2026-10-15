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

@Component("trajetoAereoController")
@ViewScoped
public class TrajetoAereoController extends ServicoControllerBase<TrajetoAereoDTO, TrajetoAereoService> {

    private final VooService vooService;
    private final TarifaAereaService tarifaAereaService;
    @Getter
    @Setter
    private CompanhiaAereaDTO companhiaAerea;
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
        super(service, orcamentoService);
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

    public List<TrajetoAereoDTO> getTrajetosDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    public void novoTrecho() {
        trechoForm = new TrechoAereoDTO();
        bagagensForm.clear();
        PrimeFaces.current().executeScript("PF('trechoDialogWidget').show();");
    }

    public void companhiaAereaEvent(SelectEvent<CompanhiaAereaDTO> event) {
        this.companhiaAerea = event.getObject();
    }

    public void carregarBagagens() {
        bagagemCompanhiaAereaService.findByCompanhiaId(companhiaAerea.getId()).forEach(bagagem -> {
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
