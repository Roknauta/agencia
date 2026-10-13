package com.roknauta.agencia.dto;

import com.roknauta.agencia.util.AppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AeroportoDTO extends BaseDTO {

    private String nome;
    private CidadeDTO cidadeAtendida;
    private String codigoIata;

    public String getCidadeAndIata() {
        return AppUtils.aeroportoLabel(this);
    }
}
