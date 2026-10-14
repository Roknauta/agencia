package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HospedagemRelatorioDTO extends ServicoRelatorioDTO {

    private String nome;
    private String cidade;
    private String endereco;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private long noites;
    private String acomodacao;
    private int quartos;
    private String regime;
    private String incluidos;
    private String naoIncluidos;
    private String cancelamento;
    private String localizador;
    private List<FotoHospedagemRelatorioDTO> fotos = new ArrayList<>();
    private List<FotoHospedagemRelatorioDTO> paresFotos = new ArrayList<>();

    public HospedagemRelatorioDTO(HospedagemDTO hospedagem) {
        super(hospedagem);
        if (hospedagem == null) {
            return;
        }
        if (hospedagem.getHotelSelecionado() != null) {
            nome = hospedagem.getHotelSelecionado().getNome();
            cidade = new CidadeRelatorioDTO(hospedagem.getHotelSelecionado().getCidade()).getNome();
            endereco = hospedagem.getHotelSelecionado().getEndereco();
        }
        checkIn = hospedagem.getCheckIn();
        checkOut = hospedagem.getCheckOut();
        if (checkIn != null && checkOut != null) {
            noites =
                Math.max(0, java.time.temporal.ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate()));
        }
        acomodacao = hospedagem.getAcomodacao();
        quartos = hospedagem.getQuartos();
        regime = hospedagem.getRegime() == null ? null : hospedagem.getRegime().getDescricao();
        incluidos = hospedagem.getIncluidos();
        naoIncluidos = hospedagem.getNaoIncluidos();
        cancelamento = hospedagem.getRegrasCancelamento();
        localizador = hospedagem.getLocalizador();
        List<FotoHospedagemDTO> fotos = hospedagem.getFotos() == null ? List.of() : hospedagem.getFotos();
        for (int i = 0; i < fotos.size(); i += 2) {
            FotoHospedagemRelatorioDTO foto =
                new FotoHospedagemRelatorioDTO(fotos.get(i), i + 1 < fotos.size() ? fotos.get(i + 1) : null);
            paresFotos.add(foto);
            this.fotos.add(foto);
        }
    }
}
