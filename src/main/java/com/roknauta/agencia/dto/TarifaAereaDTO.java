package com.roknauta.agencia.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class TarifaAereaDTO extends BaseDTO {

    private String nome;
    private CompanhiaAereaDTO companhiaAerea;
    private LocalDate dataReferencia;
    private List<BagagemCompanhiaAereaDTO> bagagensInclusas;
    private String regraCancelamento;
    private String regraAlteracao;
    private String outrasInformacoes;
}
