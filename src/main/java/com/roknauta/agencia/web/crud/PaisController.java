package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.PaisDTO;
import com.roknauta.agencia.service.PaisService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;


@Component("paisController")
@ViewScoped
public class PaisController extends CrudControllerBase<PaisDTO, PaisService> {

    public PaisController(PaisService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("pais");
    }

    public List<PaisDTO> completePais(String nome) {
        return service.findByNome(nome);
    }
}
