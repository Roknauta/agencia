package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.domain.documento.RegimeHospedagem;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import java.util.List;
import com.roknauta.agencia.service.documento.HospedagemService;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Component;

@Component("hospedagemController")
@ViewScoped
public class HospedagemController extends ServicoControllerBase<HospedagemDTO, HospedagemService> {

    public HospedagemController(HospedagemService service, OrcamentoService orcamentoService) {
        super(service, orcamentoService);
    }

    @Override
    public String pageTitle() {
        return Msg.get("hospedagem");
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

    @Override
    protected List<HospedagemDTO> buscarServicosDoOrcamento(Long idOrcamento) {
        return service.findByOrcamento(idOrcamento);
    }

    @Override
    protected HospedagemDTO salvarNoOrcamento(Long idOrcamento, HospedagemDTO servico) {
        return service.salvarNoOrcamento(idOrcamento, servico);
    }

    @Override
    protected void excluirDoOrcamento(Long idOrcamento, Long idServico) {
        service.excluirDoOrcamento(idOrcamento, idServico);
    }
}
