package com.roknauta.agencia.web.crud;

import java.util.List;

import com.roknauta.agencia.dto.HotelDTO;

import com.roknauta.agencia.service.HotelService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import jakarta.faces.view.ViewScoped;

@Component
@ViewScoped
public class HotelController extends CrudControllerBase<HotelDTO, HotelService> {

    public HotelController(HotelService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("hotel");
    }

    public List<HotelDTO> completeHotel(String texto) {
        return service.findByTexto(texto);
    }
}
