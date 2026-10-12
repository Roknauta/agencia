package com.roknauta.agencia.domain.documento;

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
public class BilheteAereo extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "viajante_id", nullable = false)
    private Viajante viajante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trecho_aereo_id", nullable = false)
    private TrechoAereo trechoAereo;

    @Column(length = 30)
    private String numeroBilhete;

    @Column(length = 10)
    private String assento;
}
