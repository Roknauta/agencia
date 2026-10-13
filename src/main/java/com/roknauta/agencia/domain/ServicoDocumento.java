package com.roknauta.agencia.domain;

public enum ServicoDocumento {

    AEREO("Passagem aérea"), HOSPEDAGEM("Hospedagem"), RODOVIARIO("Passagem rodoviária"), TRANSFER(
        "Transfer"), SEGURO_VIAGEM("Seguro viagem"), ALUGUEL_CARRO("Aluguel de carro"), CHIP("Chip internacional");

    private final String label;

    ServicoDocumento(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isRodoviario() {
        return this == RODOVIARIO;
    }
}
