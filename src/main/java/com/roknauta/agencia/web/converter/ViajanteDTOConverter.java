package com.roknauta.agencia.web.converter;

import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.service.documento.ViajanteService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import org.springframework.stereotype.Component;

@Component("viajanteDTOConverter")
public class ViajanteDTOConverter implements Converter<ViajanteDTO> {

    private final ViajanteService viajanteService;

    public ViajanteDTOConverter(ViajanteService viajanteService) {
        this.viajanteService = viajanteService;
    }

    @Override
    public ViajanteDTO getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            Long id = Long.parseLong(value);
            return viajanteService.buscarPorId(id);
        } catch (NumberFormatException e) {
            throw new ConverterException(
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Identificador de viajante inválido.", null), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ViajanteDTO value) {
        return value == null || value.getId() == null ? "" : value.getId().toString();
    }
}
