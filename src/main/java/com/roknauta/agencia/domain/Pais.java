package com.roknauta.agencia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Pais extends EntidadeBase {

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(length = 5)
    private String sigla;
}
