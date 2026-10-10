package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.CidadeDTO;

import com.roknauta.agencia.service.CidadeService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("cidadeConverter")
public class CidadeConverter extends CrudConverterBase<CidadeDTO, CidadeService> {

    public CidadeConverter(CidadeService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, CidadeDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
