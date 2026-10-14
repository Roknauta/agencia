package com.roknauta.agencia.web.jasper.documento.comum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeguroViagemRelatorioDTO {

    private String seguradora;
    private String plano;
    private String resumoCobertura;
    private String observacoes;
    private String numeroApolice;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
    private BigDecimal valorTotal;
    private String viajantesCobertos;

    public SeguroViagemRelatorioDTO(SeguroViagemDTO seguro) {
        if (seguro == null) {
            return;
        }
        seguradora = seguro.getSeguradora();
        plano = seguro.getPlano();
        resumoCobertura = seguro.getResumoCobertura();
        observacoes = seguro.getObservacoes();
        numeroApolice = seguro.getNumeroApolice();
        inicioVigencia = seguro.getInicioVigencia();
        fimVigencia = seguro.getFimVigencia();
        valorTotal = seguro.getValorTotal();
        List<com.roknauta.agencia.dto.documento.ViajanteDTO> viajantes = seguro.getViajantesCobertos();
        viajantesCobertos = viajantes == null ? ""
            : viajantes.stream().filter(viajante -> viajante != null && viajante.getNome() != null)
                .map(com.roknauta.agencia.dto.documento.ViajanteDTO::getNome).collect(Collectors.joining(", "));
    }
}
