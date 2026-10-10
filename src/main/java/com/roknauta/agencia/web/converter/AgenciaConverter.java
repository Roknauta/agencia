package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.service.AgenciaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("agenciaConverter")
public class AgenciaConverter extends CrudConverterBase<AgenciaDTO, AgenciaService> {

    public AgenciaConverter(AgenciaService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, AgenciaDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
