package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class DocumentoViagem extends EntidadeBase {

    @Column(nullable = false, unique = true, length = 50)
    private String numero;
    @Column(length = 200)
    private String cliente;
    @Column(length = 200)
    private String consultor;
    @Column
    private LocalDate dataDocumento;
    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    private Moeda moeda = Moeda.BRL;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<Transfer> transfers = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<Hospedagem> hospedagens = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<AluguelVeiculo> alugueis = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<SeguroViagem> segurosViagem = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<ESim> eSims = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private Cidade origem;
    @Column(length = 200)
    private String destino;
    private LocalDate inicio;
    private LocalDate fim;
    @Column(columnDefinition = "clob")
    private String observacoes;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<Viajante> viajantes = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<TrajetoAereo> trajetosAereos = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "ordem")
    private List<TrajetoRodoviario> trajetosRodoviarios = new ArrayList<>();
}
