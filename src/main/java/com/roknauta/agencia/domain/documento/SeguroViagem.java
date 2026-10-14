package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.AssociationOverride;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "seguro_viagem_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class SeguroViagem extends Servico {

    @Column(length = 200)
    private String seguradora;
    @Column(length = 200)
    private String plano;
    @Column(columnDefinition = "clob")
    private String resumoCobertura;
    @Column(length = 100)
    private String numeroApolice;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;

    @ManyToMany
    @JoinTable(name = "seguro_viagem_viajantes", joinColumns = @JoinColumn(name = "seguro_viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "viajante_id"))
    private List<Viajante> viajantesCobertos = new ArrayList<>();
}
