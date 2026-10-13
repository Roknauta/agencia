package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.Cidade;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class TrajetoRodoviario extends EntidadeBase {

    @Column(length = 200)
    private String transportadora;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cidade origem;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cidade destino;
    @Column(length = 200)
    private String terminalEmbarque;
    @Column(length = 200)
    private String terminalDesembarque;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private CategoriaTrajetoRodoviario categoria;
    @Column(length = 100)
    private String localizador;
    @Column(length = 1000)
    private String bagagem;
    private LocalDateTime saida;
    private LocalDateTime chegada;
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;
}
