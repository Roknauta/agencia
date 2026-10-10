package com.roknauta.agencia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoBagagem {
    ITEM_PESSOAL("Item Pessoal"),
    BAGAGEM_DE_MAO("Bagagem de mão"),
    BAGAGEM_DESPACHADA("Bagagem despachada"),
    BAGAGEM_ESPECIAL("Bagagem especial");

    private final String label;

}
