package com.roknauta.agencia.dto.documento;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmissaoDTO extends DocumentoViagemDTO {
    private ContratoDTO contrato;
    private List<BilheteAereoDTO> bilhetesAereos = new ArrayList<>();
}
