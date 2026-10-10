package com.roknauta.agencia.domain.documento;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Contrato extends DocumentoViagem {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StatusContrato status = StatusContrato.AGUARDANDO_ACEITE;

    private Instant dataConsultaPrecos;

    @Column(length = 100)
    private String referenciaPreReserva;

    private Instant preReservaValidaAte;

    private Instant dataAceite;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CanalAceiteContrato canalAceite;

    @Column(length = 500)
    private String referenciaAceite;

    private Instant dataPagamentoConfirmado;

    private Instant dataAssinatura;

    @Column(columnDefinition = "clob")
    private String regras;

}
