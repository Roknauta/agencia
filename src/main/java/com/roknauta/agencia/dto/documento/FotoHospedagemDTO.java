package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.BaseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FotoHospedagemDTO extends BaseDTO {

    private byte[] conteudo;
    private HospedagemDTO hospedagem;
}
