package com.roknauta.agencia.domain.documento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusContrato {

    AGUARDANDO_ACEITE("Aguardando aceite do cliente"), ACEITO_AGUARDANDO_PAGAMENTO(
        "Aceito, aguardando pagamento"), PAGAMENTO_CONFIRMADO(
            "Pagamento confirmado"), CANCELADO("Cancelado"), EXPIRADO("Expirado");

    private final String descricao;
}
