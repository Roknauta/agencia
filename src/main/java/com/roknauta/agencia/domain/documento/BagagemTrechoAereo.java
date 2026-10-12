package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BagagemTrechoAereo extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bagagem_companhia_aerea_id", nullable = false)
    private BagagemCompanhiaAerea bagagemCompanhiaAerea;

    private int quantidade = 1;

    @Column(length = 500)
    private String descricao;
}
