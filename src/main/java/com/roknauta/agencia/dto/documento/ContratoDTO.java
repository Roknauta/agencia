package com.roknauta.agencia.dto.documento;

import java.time.Instant;

import com.roknauta.agencia.domain.documento.CanalAceiteContrato;
import com.roknauta.agencia.domain.documento.StatusContrato;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContratoDTO extends DocumentoViagemDTO {
    private OrcamentoDTO orcamento;
    private StatusContrato status;
    private Instant dataConsultaPrecos;
    private String referenciaPreReserva;
    private Instant preReservaValidaAte;
    private Instant dataAceite;
    private CanalAceiteContrato canalAceite;
    private String referenciaAceite;
    private Instant dataPagamentoConfirmado;
    private Instant dataAssinatura;
    private String regras;
}
