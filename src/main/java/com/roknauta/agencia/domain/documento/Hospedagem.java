package com.roknauta.agencia.domain.documento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.Hotel;
import jakarta.persistence.CascadeType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinTable;
import jakarta.persistence.AssociationOverride;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AttributeOverrides({
    @AttributeOverride(name = "regrasCancelamento", column = @Column(name = "cancelamento", columnDefinition = "clob")),
    @AttributeOverride(name = "observacoes", column = @Column(name = "observacoes", columnDefinition = "clob"))})
@AssociationOverride(name = "taxas", joinTable = @JoinTable(name = "hospedagem_taxas",
    joinColumns = @JoinColumn(name = "servico_id"), inverseJoinColumns = @JoinColumn(name = "taxa_id")))
public class Hospedagem extends Servico {

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotelSelecionado;
    @OneToMany(mappedBy = "hospedagem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FotoHospedagem> fotos = new ArrayList<>();
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    @Column(length = 100)
    private String localizador;
    @Column(length = 500)
    private String acomodacao;
    private int quartos = 1;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private RegimeHospedagem regime;
    @Column(columnDefinition = "clob")
    private String incluidos;
    @Column(columnDefinition = "clob")
    private String naoIncluidos;

}
