package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.service.documento.ContratoService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("contratoConverter")
public class ContratoConverter extends CrudConverterBase<ContratoDTO, ContratoService> {

    public ContratoConverter(ContratoService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ContratoDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
