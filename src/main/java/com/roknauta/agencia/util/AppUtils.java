package com.roknauta.agencia.util;

import com.roknauta.agencia.dto.AeroportoDTO;

public class AppUtils {

    public static String aeroportoLabel(AeroportoDTO aeroporto){
        return aeroporto.getNome().concat(" ( ").concat(aeroporto.getCodigoIata()).concat(" )");
    }
}
