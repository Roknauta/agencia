package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import com.roknauta.agencia.domain.TipoBagagem;
import com.roknauta.agencia.service.BagagemCompanhiaAereaService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class BagagemCompanhiaAereaController
        extends CrudControllerBase<BagagemCompanhiaAereaDTO, BagagemCompanhiaAereaService> {

    public BagagemCompanhiaAereaController(BagagemCompanhiaAereaService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("bagagem.de.companhia.aerea");
    }

    public TipoBagagem[] getTiposBagagem() {
        return TipoBagagem.values();
    }

    public List<BagagemCompanhiaAereaDTO> completeBagagemCompanhiaAerea(String texto) {
        return service.findByTexto(texto);
    }
}
