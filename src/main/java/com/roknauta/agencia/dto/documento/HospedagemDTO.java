package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.RegimeHospedagem;
import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.dto.HotelDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HospedagemDTO extends BaseDTO {

    private HotelDTO hotelSelecionado;
    private List<FotoHospedagemDTO> fotos = new ArrayList<>();
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String localizador;
    private String acomodacao;
    private int quartos = 1;
    private RegimeHospedagem regime;
    private String cancelamento;
    private String incluidos;
    private String naoIncluidos;
    private BigDecimal valorTotal;
    private String observacoes;
}
