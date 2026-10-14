package com.roknauta.agencia.domain.documento;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.AssociationOverride;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "transfer_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class Transfer extends Servico {

    @Column(length = 200)
    private String prestador;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TipoTransfer tipo;
    @Column(length = 100)
    private String localizador;
    @Column(length = 1000)
    private String origem;
    @Column(length = 1000)
    private String destino;
    private LocalDateTime dataHora;
    @Column(length = 50)
    private String fusoHorario;
    private int passageiros = 1;
}
