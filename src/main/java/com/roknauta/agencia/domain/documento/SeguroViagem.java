package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class SeguroViagem extends EntidadeBase {

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
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;

    @ManyToMany
    @JoinTable(name = "seguro_viagem_viajantes", joinColumns = @JoinColumn(name = "seguro_viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "viajante_id"))
    private List<Viajante> viajantesCobertos = new ArrayList<>();
}
