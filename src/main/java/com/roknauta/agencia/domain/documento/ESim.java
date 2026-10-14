package com.roknauta.agencia.domain.documento;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.AssociationOverride;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "e_sim_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class ESim extends Servico {

    @Column(length = 200)
    private String operadora;
    @Column(length = 200)
    private String plano;
    @Column(length = 200)
    private String franquiaDados;
    @Column(length = 2000)
    private String cobertura;
    private Integer duracaoDias;
    @Column(columnDefinition = "clob")
    private String instrucoesInstalacao;
    @ElementCollection
    @CollectionTable(name = "esim_codigos_ativacao", joinColumns = @JoinColumn(name = "esim_id"))
    @Column(name = "codigo", nullable = false, length = 1000)
    @OrderColumn(name = "ordem")
    private List<String> codigosAtivacao = new ArrayList<>();
    private int quantidade = 1;
}
