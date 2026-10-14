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
public class ESimController extends ServicoControllerBase<ESimDTO, ESimService> {

    public ESimController(ESimService service, OrcamentoService orcamentoService) {
        super(service, orcamentoService);
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
    protected Long localizarOrcamento(Long idServico) {
        return service.findIdByESim(idServico);
    }

    @Override
    protected List<ESimDTO> buscarServicosDoOrcamento(Long idOrcamento) {
        return service.findByOrcamento(idOrcamento);
    }

    @Override
    protected ESimDTO salvarNoOrcamento(Long idOrcamento, ESimDTO servico) {
        return service.salvarNoOrcamento(idOrcamento, servico);
    }

    @Override
    protected void excluirDoOrcamento(Long idOrcamento, Long idServico) {
        service.excluirDoOrcamento(idOrcamento, idServico);
    }

    public void editarESim(Long id) {
        edit(id);
    }

    public void selecionarOrcamentoNoForm() {
        selecionarOrcamento();
    }

    public List<String> completeCodigoAtivacao(String texto) {
        return texto == null || texto.isBlank() ? List.of() : List.of(texto.trim());
    }

}
