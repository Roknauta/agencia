package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservaDTO extends BaseDTO {
    private String localizador;
    private List<TrechoReservaDTO> trechos = new ArrayList<>();
    private List<BagagemReservaDTO> bagagens = new ArrayList<>();
}
