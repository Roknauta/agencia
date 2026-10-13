package com.roknauta.agencia.domain.documento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusOrcamento {

    RASCUNHO("Rascunho"), ENVIADO("Enviado ao cliente"), CLIENTE_INTERESSADO(
        "Cliente deseja prosseguir"), CONVERTIDO_EM_CONTRATO(
            "Contrato gerado"), CANCELADO("Cancelado"), EXPIRADO("Expirado");

    private final String descricao;
}
