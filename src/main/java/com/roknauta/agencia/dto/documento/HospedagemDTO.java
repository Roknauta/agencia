package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.RegimeHospedagem;
import com.roknauta.agencia.dto.HotelDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HospedagemDTO extends ServicoDTO {

    private HotelDTO hotelSelecionado;
    private List<FotoHospedagemDTO> fotos = new ArrayList<>();
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String localizador;
    private String acomodacao;
    private int quartos = 1;
    private RegimeHospedagem regime;
    private String incluidos;
    private String naoIncluidos;
}
