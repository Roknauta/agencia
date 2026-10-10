package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.TarifaAereaDTO;
import com.roknauta.agencia.service.TarifaAereaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("tarifaAereaConverter")
public class TarifaAereaConverter extends CrudConverterBase<TarifaAereaDTO, TarifaAereaService> {

    public TarifaAereaConverter(TarifaAereaService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TarifaAereaDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
