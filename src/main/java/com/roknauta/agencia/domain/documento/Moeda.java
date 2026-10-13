package com.roknauta.agencia.domain.documento;

public enum Moeda {

    BRL("Real brasileiro", "R$");

    private final String descricao;
    private final String simbolo;

    Moeda(String descricao, String simbolo) {
        this.descricao = descricao;
        this.simbolo = simbolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
