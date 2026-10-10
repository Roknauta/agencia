package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.SentidoVoo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class TrajetoAereo extends EntidadeBase {

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private SentidoVoo sentido;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trajeto_aereo_id")
    @OrderColumn(name = "ordem")
    private List<Reserva> reservas = new ArrayList<>();
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;
}
