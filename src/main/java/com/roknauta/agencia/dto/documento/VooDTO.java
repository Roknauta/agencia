package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.dto.CompanhiaAereaDTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.roknauta.agencia.util.AppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VooDTO extends BaseDTO {
    private String numero;
    private LocalDate dataReferencia;
    private CompanhiaAereaDTO companhiaAerea;
    private AeroportoDTO origem;
    private AeroportoDTO destino;
    private LocalTime partida;
    private LocalTime chegada;

    public String autoCompleteLabel(){
        return getNumero().concat(" - ").concat(AppUtils.aeroportoLabel(origem)).concat(" - ").concat(AppUtils.aeroportoLabel(destino));
    }
}
