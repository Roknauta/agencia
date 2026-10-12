package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.CabineAerea;
import com.roknauta.agencia.domain.EntidadeBase;
import com.roknauta.agencia.domain.TarifaAerea;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrechoAereo extends EntidadeBase {

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

    @Column(length = 50)
    private String localizador;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trecho_aereo_id")
    @OrderColumn(name = "ordem")
    private List<BagagemTrechoAereo> bagagens = new ArrayList<>();
}
