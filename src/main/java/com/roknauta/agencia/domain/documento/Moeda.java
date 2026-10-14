package com.roknauta.agencia.domain.documento;

public enum Moeda {

    BRL("Real brasileiro", "R$"), USD("Dólar americano", "US$"), EUR("Euro", "€"), GBP("Libra esterlina", "£"), CAD(
        "Dólar canadense", "CA$"), AUD("Dólar australiano", "AU$"), JPY("Iene japonês", "¥"), CHF("Franco suíço",
            "CHF"), ARS("Peso argentino", "ARS$"), CLP("Peso chileno", "CLP$"), COP("Peso colombiano", "COP$");

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
