package com.roknauta.agencia.web.crud;

import java.util.List;
import java.util.ArrayList;

import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.service.documento.SeguroViagemService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("seguroViagemController")
@ViewScoped
public class SeguroViagemController extends ServicoControllerBase<SeguroViagemDTO, SeguroViagemService> {

    public SeguroViagemController(SeguroViagemService service, OrcamentoService orcamentoService) {
        super(service, orcamentoService);
    }

    @Override
    public String pageTitle() {
        return Msg.get("seguro.viagem");
    }

    @Override
    public void novo() {
        super.novo();
        if (idOrcamento == null) {
            orcamento = null;
        } else {
            orcamento = orcamentoService.findById(idOrcamento);
        }
    }

    @Override
    protected void aoSelecionarOrcamento() {
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
    }

    public List<ViajanteDTO> completeViajante(String texto) {
        if (idOrcamento == null) {
            return List.of();
        }
        return orcamentoService.findViajantes(idOrcamento, texto);
    }

    @Override
    protected Long localizarOrcamento(Long idServico) {
        return service.findIdBySeguroViagem(idServico);
    }

    @Override
    protected List<SeguroViagemDTO> buscarServicosDoOrcamento(Long idOrcamento) {
        return service.findByOrcamento(idOrcamento);
    }

    @Override
    protected SeguroViagemDTO salvarNoOrcamento(Long idOrcamento, SeguroViagemDTO servico) {
        return service.salvarNoOrcamento(idOrcamento, servico);
    }

    @Override
    protected void excluirDoOrcamento(Long idOrcamento, Long idServico) {
        service.excluirDoOrcamento(idOrcamento, idServico);
    }

    public void editarSeguro(Long id) {
        edit(id);
    }

    public List<ViajanteDTO> getViajantesDoOrcamento() {
        return idOrcamento == null ? List.of() : orcamentoService.findById(idOrcamento).getViajantes();
    }

}
