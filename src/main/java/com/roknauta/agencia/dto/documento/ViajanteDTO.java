package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.TipoDocumentoViajante;
import com.roknauta.agencia.dto.BaseDTO;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViajanteDTO extends BaseDTO {

    private String nome;
    private TipoDocumentoViajante tipoDocumento;
    private String documento;
    private LocalDate dataNascimento;
}
