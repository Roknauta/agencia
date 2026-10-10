package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.service.TarifaAereaService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class TarifaAereaController extends CrudControllerBase<TarifaAereaDTO, TarifaAereaService> {

    public TarifaAereaController(TarifaAereaService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("tarifa.aerea");
    }

    public List<TarifaAereaDTO> completeTarifaAerea(String texto) {
        return service.findByNome(texto);
    }
}
