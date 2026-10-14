package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Taxa extends EntidadeBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TipoTaxa tipoTaxa;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;
    @Column(length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    private Moeda moeda = Moeda.BRL;
}
