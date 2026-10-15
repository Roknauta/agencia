package com.roknauta.agencia.web.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value, FORMATO);
        } catch (RuntimeException e) {
            throw new ConverterException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data inválida. Use o formato dd-MM-yyyy.", null), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        return value == null ? "" : FORMATO.format(value);
    }
}
