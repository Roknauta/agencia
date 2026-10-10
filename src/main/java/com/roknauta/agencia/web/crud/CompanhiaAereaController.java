package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.service.CompanhiaAereaService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class CompanhiaAereaController extends CrudControllerBase<CompanhiaAereaDTO, CompanhiaAereaService> {

    public CompanhiaAereaController(CompanhiaAereaService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("companhia.aerea");
    }

    public List<CompanhiaAereaDTO> completeCompanhiaAerea(String nome) {
        return service.findByNome(nome);
    }
}
