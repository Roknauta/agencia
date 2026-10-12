package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BilheteAereoDTO extends BaseDTO {
    private ViajanteDTO viajante;
    private TrechoAereoDTO trechoAereo;
    private String numeroBilhete;
    private String assento;
}
