package com.roknauta.agencia.domain.documento;

public enum LocadoraVeiculo {

    UNIDAS("Unidas"), LOCALIZA("Localiza"), MOVIDA("Movida");

    private final String label;

    LocadoraVeiculo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }


}
