package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.service.AgenciaService;
import com.roknauta.agencia.web.Msg;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component("agenciaController")
@SessionScope
public class AgenciaController extends CrudControllerBase<AgenciaDTO, AgenciaService> {

    public AgenciaController(AgenciaService service) {
        super(service);
    }

    @PostConstruct
    public void carregarConfiguracao() {
        if (getSearchForm() == null) {
            super.inicializar();
        }
        editForm = service.configuracao();
        editing = true;
    }

    @Override
    public String pageTitle() {
        return Msg.get("agencia");
    }

    @Override
    public void save() {
        editForm = service.save(editForm);
        editing = true;
        addInfoMessage("entidade.salva", pageTitle());
    }

    @Override
    public void voltarPesquisa() {
        editForm = service.configuracao();
        editing = true;
    }
}
