package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.web.Msg;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;

public abstract class CrudConverterBase<T extends BaseDTO, S extends CrudService<T>>
        implements Converter<T> {

    protected final S service;

    protected CrudConverterBase(S service) {
        this.service = service;
    }

    @Override
    public T getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return service.findById(Long.parseLong(value));
        } catch (IllegalArgumentException e) {
            throw new ConverterException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    Msg.get("crud.converter.valor.invalido"),
                    null
            ));
        }
    }

    @Override
    public abstract String getAsString(FacesContext context, UIComponent component, T value);
}
