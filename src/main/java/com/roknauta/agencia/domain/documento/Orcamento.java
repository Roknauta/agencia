package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Orcamento extends DocumentoViagem {

    private LocalDate validadeAte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StatusOrcamento status = StatusOrcamento.RASCUNHO;

    private Instant dataEnvio;
    private Instant dataInteresseCliente;

    private Integer adultos = 1;
    private Integer criancas = 0;
    private Integer bebes = 0;
    @Column(columnDefinition = "clob")
    private String pagamento;
    @Column(columnDefinition = "clob")
    private String tarifa;
    @Column(precision = 19, scale = 2)
    private BigDecimal valorAereo;
    @Column(precision = 19, scale = 2)
    private BigDecimal taxas;
    @Column(precision = 19, scale = 2)
    private BigDecimal servicos;
    @Column(precision = 19, scale = 2)
    private BigDecimal desconto;

}
