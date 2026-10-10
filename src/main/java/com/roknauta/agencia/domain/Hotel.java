package com.roknauta.agencia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "cidade_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Hotel extends EntidadeBase {

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Cidade cidade;

    @Column(nullable = false, length = 200)
    private String endereco;
}
