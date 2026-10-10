package com.roknauta.agencia.domain.documento;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FotoHospedagem extends EntidadeBase {

    @Lob
    private byte[] conteudo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospedagem_id", nullable = false)
    private Hospedagem hospedagem;
}
