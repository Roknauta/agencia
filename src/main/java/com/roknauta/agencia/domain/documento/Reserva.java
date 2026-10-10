package com.roknauta.agencia.domain.documento;

import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Reserva extends EntidadeBase {
    @Column(length = 50)
    private String localizador;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reserva_id")
    @OrderColumn(name = "ordem")
    private List<TrechoReserva> trechos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "reserva_id")
    @OrderColumn(name = "ordem")
    private List<BagagemReserva> bagagens = new ArrayList<>();

}
