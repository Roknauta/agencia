package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.documento.TipoTaxaDTO;
import com.roknauta.agencia.service.documento.TipoTaxaService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

@Component("tipoTaxaController")
@ViewScoped
public class TipoTaxaController extends CrudControllerBase<TipoTaxaDTO, TipoTaxaService> {

    public TipoTaxaController(TipoTaxaService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("tipo.taxa");
    }

    public List<TipoTaxaDTO> completeTipoTaxa(String texto) {
        return service.findAtivosByTexto(texto);
    }
}
