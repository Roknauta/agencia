package com.roknauta.agencia.domain.documento;

public enum CategoriaVeiculo {

    ECONOMICO("Econômico"), COMPACTO("Compacto"), INTERMEDIARIO("Intermediário"), STANDARD("Standard"), FULL_SIZE(
        "Full size"), SUV("SUV"), MINIVAN("Minivan"), VAN("Van"), LUXO("Luxo"), PICKUP("Picape");

    private final String descricao;

    CategoriaVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
