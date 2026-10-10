package com.roknauta.agencia.dto.documento;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContratoDTO extends DocumentoViagemDTO {
    private OrcamentoDTO orcamento;
    private Instant dataAssinatura;
    private String regras;
    private DadosAgenciaContratoDTO dadosAgencia;
}
