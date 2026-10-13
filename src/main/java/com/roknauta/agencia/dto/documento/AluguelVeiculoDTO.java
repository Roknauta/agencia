package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.CategoriaVeiculo;
import com.roknauta.agencia.domain.documento.LocadoraVeiculo;
import com.roknauta.agencia.dto.BaseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AluguelVeiculoDTO extends BaseDTO {

    private LocadoraVeiculo locadora;
    private CategoriaVeiculo categoria;
    private List<String> condutores = new ArrayList<>();
    private String localizador;
    private String localRetirada;
    private String localDevolucao;
    private String coberturas;
    private String condicoes;
    private String observacoes;
    private LocalDateTime retirada;
    private LocalDateTime devolucao;
    private String fusoHorarioRetirada;
    private String fusoHorarioDevolucao;
    private BigDecimal valorTotal;
}
