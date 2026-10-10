package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.HotelDTO;

import com.roknauta.agencia.service.HotelService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import org.springframework.stereotype.Component;

@Component("hotelConverter")
public class HotelConverter extends CrudConverterBase<HotelDTO, HotelService> {

    public HotelConverter(HotelService service) {
        super(service);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, HotelDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
