package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.EntidadeBase;
import com.roknauta.agencia.domain.Cidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;

    @Column(length = 200)
    private String razaoSocial;

    @Column(length = 200)
    private String nomeFantasia;

    @Column(length = 18)
    private String cnpj;

    @Column(length = 500)
    private String endereco;

    @Column(length = 254)
    private String email;

    @Column(length = 30)
    private String telefone;

    @Column(length = 200)
    private String representante;

    @Column(length = 30)
    private String cadastur;

    @Column(length = 500)
    private String horario;

    @Column(length = 200)
    private String urgencia;
}
