package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.PaisDTO;
import com.roknauta.agencia.service.PaisService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("paisConverter")
public class PaisConverter extends CrudConverterBase<PaisDTO, PaisService> {

    public PaisConverter(PaisService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PaisDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }

}
