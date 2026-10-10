package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.BagagemCompanhiaAereaDTO;
import com.roknauta.agencia.service.BagagemCompanhiaAereaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("bagagemCompanhiaAereaConverter")
public class BagagemCompanhiaAereaConverter
        extends CrudConverterBase<BagagemCompanhiaAereaDTO, BagagemCompanhiaAereaService> {

    public BagagemCompanhiaAereaConverter(BagagemCompanhiaAereaService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, BagagemCompanhiaAereaDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
