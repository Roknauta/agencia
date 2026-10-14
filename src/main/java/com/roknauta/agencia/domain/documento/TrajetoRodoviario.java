package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.Cidade;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.AssociationOverride;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "trajeto_rodoviario_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class TrajetoRodoviario extends Servico {

    @Column(length = 200)
    private String transportadora;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cidade origem;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cidade destino;
    @Column(length = 200)
    private String terminalEmbarque;
    @Column(length = 200)
    private String terminalDesembarque;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private CategoriaTrajetoRodoviario categoria;
    @Column(length = 100)
    private String localizador;
    @Column(length = 1000)
    private String bagagem;
    private LocalDateTime saida;
    private LocalDateTime chegada;
}
