package com.roknauta.agencia.domain.documento;

import java.time.LocalDate;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Viajante extends EntidadeBase {

    @Column(length = 200)
    private String nome;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private TipoDocumentoViajante tipoDocumento;
    @Column(length = 50)
    private String documento;
    private LocalDate dataNascimento;
}
