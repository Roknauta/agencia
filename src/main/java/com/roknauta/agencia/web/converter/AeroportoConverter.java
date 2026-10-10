package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.service.AeroportoService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("aeroportoConverter")
public class AeroportoConverter extends CrudConverterBase<AeroportoDTO, AeroportoService> {

    public AeroportoConverter(AeroportoService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, AeroportoDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
