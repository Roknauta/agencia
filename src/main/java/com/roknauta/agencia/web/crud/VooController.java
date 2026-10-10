package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.documento.VooDTO;
import com.roknauta.agencia.service.documento.VooService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Component
@SessionScope
public class VooController extends CrudControllerBase<VooDTO, VooService> {

    public VooController(VooService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("voo");
    }

    public List<VooDTO> completeVoo(String numero) {
        return service.findByNumero(numero);
    }
}
