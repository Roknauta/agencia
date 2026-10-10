package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.documento.VooDTO;
import com.roknauta.agencia.service.documento.VooService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("vooConverter")
public class VooConverter extends CrudConverterBase<VooDTO, VooService> {

    public VooConverter(VooService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, VooDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
