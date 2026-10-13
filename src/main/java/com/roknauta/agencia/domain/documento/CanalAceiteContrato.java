package com.roknauta.agencia.domain.documento;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CanalAceiteContrato {

    WHATSAPP("WhatsApp"), EMAIL("E-mail");

    private final String descricao;
}
