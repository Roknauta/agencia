package com.roknauta.agencia.web.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("localDateTimeGridConverter")
public class LocalDateTimeGridConverter implements Converter<LocalDateTime> {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, FORMATO);
        } catch (RuntimeException e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Data e hora inválidas. Use o formato dd-MM-yyyy HH:mm.", null), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {
        return value == null ? "" : FORMATO.format(value);
    }
}
