package com.roknauta.agencia.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgenciaDTO extends BaseDTO {
    private String agenciaRazaoSocial;
    private String agenciaNomeFantasia;
    private String agenciaCnpj;
    private String agenciaEndereco;
    private String agenciaEmail;
    private String agenciaTelefone;
    private String agenciaRepresentante;
    private String agenciaCadastur;
    private String agenciaHorario;
    private String agenciaUrgencia;
}
