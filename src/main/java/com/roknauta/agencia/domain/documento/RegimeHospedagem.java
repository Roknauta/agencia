package com.roknauta.agencia.domain.documento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegimeHospedagem {
    SOMENTE_HOSPEDAGEM("Somente hospedagem"),
    CAFE_DA_MANHA("Café da manhã"),
    MEIA_PENSAO("Meia pensão"),
    PENSAO_COMPLETA("Pensão completa"),
    ALL_INCLUSIVE("All inclusive");

    private final String descricao;
}
