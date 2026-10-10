package com.roknauta.agencia.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CabineAerea {
    ECONOMICA("Econômica"),
    PREMIUM_ECONOMICA("Premium econômica"),
    EXECUTIVA("Executiva"),
    PRIMEIRA_CLASSE("Primeira classe");

    private final String descricao;
}
