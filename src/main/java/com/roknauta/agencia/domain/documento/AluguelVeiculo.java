package com.roknauta.agencia.domain.documento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.domain.EntidadeBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class AluguelVeiculo extends EntidadeBase {

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private LocadoraVeiculo locadora;
    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private CategoriaVeiculo categoria;
    @ElementCollection
    @CollectionTable(name = "aluguel_veiculo_condutores",
            joinColumns = @JoinColumn(name = "aluguel_veiculo_id"))
    @Column(name = "condutor", nullable = false, length = 200)
    private List<String> condutores = new ArrayList<>();
    @Column(length = 50)
    private String localizador;
    @Column(length = 1000)
    private String localRetirada;
    @Column(length = 1000)
    private String localDevolucao;
    @Column(columnDefinition = "clob")
    private String coberturas;
    @Column(columnDefinition = "clob")
    private String condicoes;
    @Column(columnDefinition = "clob")
    private String observacoes;
    private LocalDateTime retirada;
    private LocalDateTime devolucao;
    @Column(length = 50)
    private String fusoHorarioRetirada;
    @Column(length = 50)
    private String fusoHorarioDevolucao;
    @Column(precision = 19, scale = 2)
    private BigDecimal valorTotal;
}
