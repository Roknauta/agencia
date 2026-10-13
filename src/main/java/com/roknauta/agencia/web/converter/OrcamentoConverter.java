package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.service.documento.OrcamentoService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("orcamentoConverter")
public class OrcamentoConverter extends CrudConverterBase<OrcamentoDTO, OrcamentoService> {

    public OrcamentoConverter(OrcamentoService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, OrcamentoDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
