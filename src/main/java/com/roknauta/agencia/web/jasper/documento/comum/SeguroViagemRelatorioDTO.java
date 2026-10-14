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
public class SeguroViagemRelatorioDTO extends ServicoRelatorioDTO {

    private String seguradora;
    private String plano;
    private String resumoCobertura;
    private String numeroApolice;
    private LocalDate inicioVigencia;
    private LocalDate fimVigencia;
    private String viajantesCobertos;

    public SeguroViagemRelatorioDTO(SeguroViagemDTO seguro) {
        super(seguro);
        if (seguro == null) {
            return;
        }
        seguradora = seguro.getSeguradora();
        plano = seguro.getPlano();
        resumoCobertura = seguro.getResumoCobertura();
        numeroApolice = seguro.getNumeroApolice();
        inicioVigencia = seguro.getInicioVigencia();
        fimVigencia = seguro.getFimVigencia();
        List<com.roknauta.agencia.dto.documento.ViajanteDTO> viajantes = seguro.getViajantesCobertos();
        viajantesCobertos = viajantes == null ? ""
            : viajantes.stream().filter(viajante -> viajante != null && viajante.getNome() != null)
                .map(com.roknauta.agencia.dto.documento.ViajanteDTO::getNome).collect(Collectors.joining(", "));
    }

    public List<AtributoRelatorioDTO> getAtributosRelatorio() {
        return AtributoRelatorioDTO.lista(AtributoRelatorioDTO.campo("Seguradora", seguradora),
            AtributoRelatorioDTO.campo("Plano", plano), AtributoRelatorioDTO.campo("Apólice", numeroApolice),
            AtributoRelatorioDTO.campo("Início da vigência", data(inicioVigencia)),
            AtributoRelatorioDTO.campo("Fim da vigência", data(fimVigencia)),
            AtributoRelatorioDTO.campo("Viajantes cobertos", viajantesCobertos),
            AtributoRelatorioDTO.campo("Cobertura", resumoCobertura));
    }

    private static String data(LocalDate valor) {
        return valor == null ? null : valor.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
