package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Agencia extends EntidadeBase {

    @Column(nullable = false, unique = true, length = 20)
    private String chaveConfiguracao;

    @Column(length = 200)
    private String agenciaRazaoSocial;

    @Column(length = 200)
    private String agenciaNomeFantasia;

    @Column(length = 18)
    private String agenciaCnpj;

    @Column(length = 500)
    private String agenciaEndereco;

    @Column(length = 254)
    private String agenciaEmail;

    @Column(length = 30)
    private String agenciaTelefone;

    @Column(length = 200)
    private String agenciaRepresentante;

    @Column(length = 30)
    private String agenciaCadastur;

    @Column(length = 500)
    private String agenciaHorario;

    @Column(length = 200)
    private String agenciaUrgencia;
}
