package com.roknauta.agencia.web.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("localDateGridConverter")
public class LocalDateGridConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Este converter é somente para exibição em grids.");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        return value == null ? "" : FORMATO.format(value);
    }
}
