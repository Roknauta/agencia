package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.CompanhiaAereaDTO;
import com.roknauta.agencia.service.CompanhiaAereaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("companhiaAereaConverter")
public class CompanhiaAereaConverter extends CrudConverterBase<CompanhiaAereaDTO, CompanhiaAereaService> {

    public CompanhiaAereaConverter(CompanhiaAereaService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CompanhiaAereaDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
