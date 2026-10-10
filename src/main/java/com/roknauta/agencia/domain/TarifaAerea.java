package com.roknauta.agencia.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
        "nome", "companhia_aerea_id", "data_referencia"
}))
@Getter
@Setter
@NoArgsConstructor
public class TarifaAerea extends EntidadeBase {

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CompanhiaAerea companhiaAerea;

    @Column(nullable = false)
    private LocalDate dataReferencia;

    @ManyToMany
    @JoinTable(name = "tarifa_aerea_bagagens_inclusas",
            joinColumns = @JoinColumn(name = "tarifa_aerea_id"),
            inverseJoinColumns = @JoinColumn(name = "bagagem_companhia_aerea_id"))
    private List<BagagemCompanhiaAerea> bagagensInclusas = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "clob")
    private String regraCancelamento;

    @Column(nullable = false, columnDefinition = "clob")
    private String regraAlteracao;

    @Column(columnDefinition = "clob")
    private String outrasInformacoes;
}
