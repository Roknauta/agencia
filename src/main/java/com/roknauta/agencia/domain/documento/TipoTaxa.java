package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TipoTaxa extends EntidadeBase {

    @Column(nullable = false, unique = true, length = 100)
    private String nome;
    @Column(length = 1000)
    private String descricao;
    @Column(nullable = false)
    private boolean ativo = true;
}
