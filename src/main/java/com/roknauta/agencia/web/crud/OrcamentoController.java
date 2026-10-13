package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.CidadeDTO;
import com.roknauta.agencia.service.documento.OrcamentoService;
import com.roknauta.agencia.service.CidadeService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class OrcamentoController extends CrudControllerBase<OrcamentoDTO, OrcamentoService> {

    private final CidadeService cidadeService;

    public OrcamentoController(OrcamentoService service, CidadeService cidadeService) {
        super(service);
        this.cidadeService = cidadeService;
    }

    @Override
    public String pageTitle() {
        return Msg.get("orcamento");
    }
}
