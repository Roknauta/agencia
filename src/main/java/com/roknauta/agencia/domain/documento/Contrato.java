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

    private Instant dataAssinatura;

    @Column(columnDefinition = "clob")
    private String regras;

    @Embedded
    private DadosAgenciaContrato dadosAgencia;
}
