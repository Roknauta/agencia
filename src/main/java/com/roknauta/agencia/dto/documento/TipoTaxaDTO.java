package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoTaxaDTO extends BaseDTO {

    private String nome;
    private String descricao;
    private Boolean ativo = true;
}
