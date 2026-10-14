package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Servico extends EntidadeBase {

    @Column(name = "valorTotal", precision = 19, scale = 2)
    private BigDecimal valor;
    @Column(precision = 19, scale = 2)
    private BigDecimal desconto;
    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    private Moeda moeda = Moeda.BRL;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Taxa> taxas = new ArrayList<>();
    @Column(columnDefinition = "clob")
    private String regrasAlteracao;
    @Column(columnDefinition = "clob")
    private String regrasCancelamento;
    @Column(columnDefinition = "clob")
    private String observacoes;
}
