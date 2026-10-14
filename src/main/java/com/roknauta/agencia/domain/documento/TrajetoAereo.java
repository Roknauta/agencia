package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.SentidoVoo;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "trajeto_aereo_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class TrajetoAereo extends Servico {

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private SentidoVoo sentido;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trajeto_aereo_id")
    @OrderColumn(name = "ordem")
    private List<TrechoAereo> trechos = new ArrayList<>();
}
