package com.roknauta.agencia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cidade extends EntidadeBase {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 50)
    private String estado;

    @Column(length = 50)
    private String fusoHorario;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pais pais;
}
