package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.CategoriaTrajetoRodoviario;
import com.roknauta.agencia.dto.CidadeDTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrajetoRodoviarioDTO extends ServicoDTO {

    private String transportadora;
    private CidadeDTO origem;
    private CidadeDTO destino;
    private String terminalEmbarque;
    private String terminalDesembarque;
    private CategoriaTrajetoRodoviario categoria;
    private String localizador;
    private String bagagem;
    private LocalDateTime saida;
    private LocalDateTime chegada;
}
