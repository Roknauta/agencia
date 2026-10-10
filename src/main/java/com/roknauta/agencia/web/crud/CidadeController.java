package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.dto.CidadeDTO;
import java.util.List;

import com.roknauta.agencia.service.CidadeService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class CidadeController extends CrudControllerBase<CidadeDTO, CidadeService> {

    public CidadeController(CidadeService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("cidade");
    }

    public List<CidadeDTO> completeCidade(String texto) {
        return service.findByTexto(texto);
    }
}
