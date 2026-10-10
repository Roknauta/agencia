package com.roknauta.agencia.web.crud;

import com.roknauta.agencia.dto.HotelDTO;

import com.roknauta.agencia.service.HotelService;
import com.roknauta.agencia.web.Msg;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class HotelController extends CrudControllerBase<HotelDTO, HotelService> {

    public HotelController(HotelService service) {
        super(service);
    }

    @Override
    public String pageTitle() {
        return Msg.get("hotel");
    }
}
