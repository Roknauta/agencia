package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.domain.documento.RegimeHospedagem;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.service.documento.HospedagemService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Component;

@Component
@ViewScoped
public class HospedagemController extends CrudControllerBase<HospedagemDTO, HospedagemService> {

    private final OrcamentoService orcamentoService;
    private Long idOrcamento;
    private OrcamentoDTO orcamentoSelecionado;

    public HospedagemController(HospedagemService service, OrcamentoService orcamentoService) {
        super(service);
        this.orcamentoService = orcamentoService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("hospedagem");
    }

    public List<OrcamentoDTO> completeOrcamento(String texto) {
        return orcamentoService.findByTexto(texto);
    }

    public void selecionarOrcamento() {
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
        atualizarResultados();
    }

    public RegimeHospedagem[] getRegimes() {
        return RegimeHospedagem.values();
    }

    public void adicionarFoto(FileUploadEvent event) {
        FotoHospedagemDTO foto = new FotoHospedagemDTO();
        foto.setConteudo(event.getFile().getContent());
        editForm.getFotos().add(foto);
    }

    public void removerFoto(int indice) {
        editForm.getFotos().remove(indice);
    }

    private List<HospedagemDTO> getHospedagensDoOrcamento() {
        return idOrcamento == null ? List.of() : service.findByOrcamento(idOrcamento);
    }

    @Override
    public void save() {
        if (idOrcamento == null) {
            throw new IllegalArgumentException("Selecione um orçamento.");
        }
        service.salvarNoOrcamento(idOrcamento, editForm);
        atualizarResultados();
        voltarPesquisa();
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    protected void atualizarResultados() {
        setResult(getHospedagensDoOrcamento());
        pesquisaRealizada = idOrcamento != null && CollectionUtils.isNotEmpty(getResult());
    }

    @Override
    public void delete(Long id) {
        if (idOrcamento == null) {
            return;
        }
        service.excluirDoOrcamento(idOrcamento, id);
        atualizarResultados();
    }

    public OrcamentoDTO getOrcamentoSelecionado() {
        return orcamentoSelecionado;
    }

    public void setOrcamentoSelecionado(OrcamentoDTO orcamentoSelecionado) {
        this.orcamentoSelecionado = orcamentoSelecionado;
        idOrcamento = orcamentoSelecionado == null ? null : orcamentoSelecionado.getId();
    }
}
