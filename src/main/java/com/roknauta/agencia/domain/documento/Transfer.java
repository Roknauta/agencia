package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Transfer extends EntidadeBase {

    @Column(length = 200)
    private String prestador;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TipoTransfer tipo;
    @Column(length = 100)
    private String localizador;
    @Column(length = 1000)
    private String origem;
    @Column(length = 1000)
    private String destino;
    private LocalDateTime dataHora;
    @Column(length = 50)
    private String fusoHorario;
    private int passageiros = 1;
    @Column(columnDefinition = "clob")
    private String observacoes;
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;
}
