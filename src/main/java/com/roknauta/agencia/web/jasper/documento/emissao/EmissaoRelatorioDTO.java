package com.roknauta.agencia.web.jasper.documento.emissao;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.roknauta.agencia.dto.documento.BilheteAereoDTO;
import com.roknauta.agencia.dto.documento.EmissaoDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.web.jasper.documento.comum.DocumentoViagemRelatorioDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmissaoRelatorioDTO extends DocumentoViagemRelatorioDTO {

    private static final DateTimeFormatter DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private String emitidoEm;
    private List<BilheteAereoRelatorioDTO> passageiros = new ArrayList<>();

    public EmissaoRelatorioDTO(EmissaoDTO emissao) {
        super(emissao);
        if (emissao == null) {
            return;
        }
        emitidoEm = emissao.getDataDocumento() == null
                ? LocalDateTimeProvider.agora()
                : emissao.getDataDocumento().atStartOfDay().format(DATA_HORA);
        List<BilheteAereoDTO> bilhetes = emissao.getBilhetesAereos() == null
                ? List.of() : emissao.getBilhetesAereos();
        if (bilhetes.isEmpty()) {
            passageiros = emissao.getViajantes() == null ? List.of() : emissao.getViajantes().stream()
                    .map(this::viajanteSemBilhete).toList();
        } else {
            passageiros = bilhetes.stream().map(BilheteAereoRelatorioDTO::new).toList();
        }
    }

    public List<com.roknauta.agencia.web.jasper.documento.comum.ReservaAereaRelatorioDTO> getReservas() {
        return getTrajetosAereos().stream()
                .filter(Objects::nonNull)
                .flatMap(trajeto -> trajeto.getReservas().stream()).toList();
    }

    public String getOrientacoes() {
        return getObservacoes();
    }

    public String getRegras() {
        return getTrajetosAereos().stream()
                .flatMap(trajeto -> trajeto.getVoos().stream())
                .flatMap(voo -> Stream.of(voo.getRegraCancelamento(), voo.getRegraAlteracao(),
                        voo.getOutrasInformacoesTarifarias()))
                .filter(texto -> texto != null && !texto.isBlank())
                .distinct()
                .collect(Collectors.joining("\n"));
    }

    private BilheteAereoRelatorioDTO viajanteSemBilhete(ViajanteDTO viajante) {
        BilheteAereoRelatorioDTO item = new BilheteAereoRelatorioDTO();
        item.setNome(viajante == null ? null : viajante.getNome());
        return item;
    }

    private static final class LocalDateTimeProvider {
        private static String agora() {
            return java.time.LocalDateTime.now().format(DATA_HORA);
        }
    }
}
