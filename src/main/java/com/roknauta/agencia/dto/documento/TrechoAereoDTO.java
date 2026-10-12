package com.roknauta.agencia.dto.documento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.dto.TarifaAereaDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrechoAereoDTO extends BaseDTO {
    private VooDTO voo;
    private LocalDate dataPartida;
    private LocalDate dataChegada;
    private TarifaAereaDTO tarifaAerea;
    private CabineAerea cabine;
    private String localizador;
    private List<BagagemTrechoAereoDTO> bagagens = new ArrayList<>();
}
