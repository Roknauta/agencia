package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.LocalDate;

import com.roknauta.agencia.dto.documento.ViajanteDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViajanteRelatorioDTO {
    private String nome;
    private String documento;
    private LocalDate dataNascimento;

    public ViajanteRelatorioDTO(ViajanteDTO viajante) {
        if (viajante == null) {
            return;
        }
        nome = viajante.getNome();
        documento = viajante.getDocumento();
        dataNascimento = viajante.getDataNascimento();
    }
}
