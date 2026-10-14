package com.roknauta.agencia.dto.documento;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeguroViagemDTO extends ServicoDTO {

    private String seguradora;
    private String plano;
    private String resumoCobertura;
    private String numeroApolice;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
    private List<ViajanteDTO> viajantesCobertos = new ArrayList<>();
}
