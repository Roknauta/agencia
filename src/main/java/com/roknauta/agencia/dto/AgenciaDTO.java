package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgenciaDTO extends BaseDTO {
    private CidadeDTO cidade;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String endereco;
    private String email;
    private String telefone;
    private String representante;
    private String cadastur;
    private String horario;
    private String urgencia;
}
