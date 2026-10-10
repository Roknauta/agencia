package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;
import java.time.LocalTime;

import com.roknauta.agencia.domain.Aeroporto;
import com.roknauta.agencia.domain.CompanhiaAerea;
import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "numero", "companhia_aerea_id", "data_referencia"
}))
@Getter
@Setter
@NoArgsConstructor
public class Voo extends EntidadeBase {

    @Column(nullable = false, length = 10)
    private String numero;

    @Column(nullable = false)
    private LocalDate dataReferencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CompanhiaAerea companhiaAerea;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Aeroporto origem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Aeroporto destino;

    @Column(nullable = false)
    private LocalTime partida;

    @Column(nullable = false)
    private LocalTime chegada;
}
