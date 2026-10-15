package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.TipoBagagem;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class BagagemTrajetoAereo {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bagagem", nullable = false, length = 50)
    private TipoBagagem tipoBagagem;

    @Column(nullable = false)
    private int quantidade = 1;
}
