package com.roknauta.agencia.web;

import jakarta.faces.view.ViewScoped;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

@Component
@ViewScoped
public class JsfHelper {

    public List<String> getFusosHorarios() {
        return ZoneId.getAvailableZoneIds().stream().sorted(Comparator.naturalOrder()).toList();
    }

}
