package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.domain.EntidadeBase;
import com.roknauta.agencia.domain.TarifaAerea;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrechoReserva extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "voo_id", nullable = false)
    private Voo voo;

    @Column(nullable = false)
    private LocalDate dataPartida;

    @Column(nullable = false)
    private LocalDate dataChegada;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarifa_aerea_id", nullable = false)
    private TarifaAerea tarifaAerea;

    @Enumerated(EnumType.STRING)
    private CabineAerea cabine;
}
