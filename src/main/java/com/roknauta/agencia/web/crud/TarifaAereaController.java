package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.service.TarifaAereaService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;


@Component("tarifaAereaController")
@ViewScoped
public class TarifaAereaController extends CrudControllerBase<TarifaAereaDTO, TarifaAereaService> {

    public TarifaAereaController(TarifaAereaService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("tarifa.aerea");
    }

    public List<TarifaAereaDTO> completeTarifaAerea(String texto) {
        return service.findByNomeCompanhiaAerea(texto, null);
    }
}
