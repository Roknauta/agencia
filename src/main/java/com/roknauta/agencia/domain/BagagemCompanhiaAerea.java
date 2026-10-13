package com.roknauta.agencia.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"companhia_aerea_id", "tipo_bagagem", "data_referencia"}))
@Getter
@Setter
@NoArgsConstructor
public class BagagemCompanhiaAerea extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CompanhiaAerea companhiaAerea;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoBagagem tipoBagagem;

    @Column(nullable = false)
    private LocalDate dataReferencia;

    @Column(nullable = false)
    private Double alturaCm;

    @Column(nullable = false)
    private Double larguraCm;

    @Column(nullable = false)
    private Double profundidadeCm;

    @Column(nullable = false)
    private Double pesoMaximo;
}
