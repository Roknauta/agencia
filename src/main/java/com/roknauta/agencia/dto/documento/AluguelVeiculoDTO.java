package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.domain.documento.CategoriaVeiculo;
import com.roknauta.agencia.domain.documento.LocadoraVeiculo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AluguelVeiculoDTO extends ServicoDTO {

    private LocadoraVeiculo locadora;
    private CategoriaVeiculo categoria;
    private List<String> condutores = new ArrayList<>();
    private String localizador;
    private String localRetirada;
    private String localDevolucao;
    private String coberturas;
    private String condicoes;
    private LocalDateTime retirada;
    private LocalDateTime devolucao;
    private String fusoHorarioRetirada;
    private String fusoHorarioDevolucao;
}
