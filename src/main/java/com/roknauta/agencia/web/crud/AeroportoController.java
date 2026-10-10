package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.service.AeroportoService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class AeroportoController extends CrudControllerBase<AeroportoDTO, AeroportoService> {

    public AeroportoController(AeroportoService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("aeroporto");
    }

    public List<AeroportoDTO> completeAeroporto(String nome) {
        return service.findByNome(nome);
    }
}
