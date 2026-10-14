package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.TipoTransfer;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransferDTO extends ServicoDTO {

    private String prestador;
    private TipoTransfer tipo;
    private String localizador;
    private String origem;
    private String destino;
    private LocalDateTime dataHora;
    private String fusoHorario;
    private int passageiros = 1;
}
