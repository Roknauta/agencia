package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.documento.TipoTaxaDTO;
import com.roknauta.agencia.service.documento.TipoTaxaService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("tipoTaxaConverter")
public class TipoTaxaConverter extends CrudConverterBase<TipoTaxaDTO, TipoTaxaService> {

    public TipoTaxaConverter(TipoTaxaService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TipoTaxaDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
