package com.roknauta.agencia.web.jasper.documento.comum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.dto.documento.DocumentoViagemDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DocumentoViagemRelatorioDTO {
    private String numero;
    private String cliente;
    private String consultor;
    private LocalDate dataDocumento;
    private String origem;
    private String destino;
    private LocalDate inicio;
    private LocalDate fim;
    private String observacoes;
    private List<ViajanteRelatorioDTO> viajantes = new ArrayList<>();
    private List<TrajetoAereoRelatorioDTO> trajetosAereos = new ArrayList<>();
    private List<TrajetoRodoviarioRelatorioDTO> trajetosRodoviarios = new ArrayList<>();
    private List<HospedagemRelatorioDTO> hospedagens = new ArrayList<>();
    private List<TransferRelatorioDTO> transfers = new ArrayList<>();
    private List<AluguelVeiculoRelatorioDTO> alugueis = new ArrayList<>();
    private List<SeguroViagemRelatorioDTO> segurosViagem = new ArrayList<>();
    private List<ESimRelatorioDTO> eSims = new ArrayList<>();

    public DocumentoViagemRelatorioDTO(DocumentoViagemDTO documento) {
        if (documento == null) {
            return;
        }
        numero = documento.getNumero();
        cliente = documento.getCliente();
        consultor = documento.getConsultor();
        dataDocumento = documento.getDataDocumento();
        origem = new CidadeRelatorioDTO(documento.getOrigem()).getNome();
        destino = new CidadeRelatorioDTO(documento.getDestino()).getNome();
        inicio = documento.getInicio();
        fim = documento.getFim();
        observacoes = documento.getObservacoes();
        if (documento.getViajantes() != null) {
            viajantes = documento.getViajantes().stream().map(ViajanteRelatorioDTO::new).toList();
        }
        if (documento.getTrajetosAereos() != null) {
            trajetosAereos = documento.getTrajetosAereos().stream()
                    .map(TrajetoAereoRelatorioDTO::new).toList();
        }
        if (documento.getTrajetosRodoviarios() != null) {
            trajetosRodoviarios = documento.getTrajetosRodoviarios().stream()
                    .map(TrajetoRodoviarioRelatorioDTO::new).toList();
        }
        if (documento.getHospedagens() != null) {
            hospedagens = documento.getHospedagens().stream().map(HospedagemRelatorioDTO::new).toList();
        }
        if (documento.getTransfers() != null) {
            transfers = documento.getTransfers().stream().map(TransferRelatorioDTO::new).toList();
        }
        if (documento.getAlugueis() != null) {
            alugueis = documento.getAlugueis().stream().map(AluguelVeiculoRelatorioDTO::new).toList();
        }
        if (documento.getSegurosViagem() != null) {
            segurosViagem = documento.getSegurosViagem().stream().map(SeguroViagemRelatorioDTO::new).toList();
        }
        if (documento.getESims() != null) {
            eSims = documento.getESims().stream().map(ESimRelatorioDTO::new).toList();
        }
    }

    public TrajetoAereoRelatorioDTO getIda() {
        return agregarTrajetos(SentidoVoo.IDA);
    }

    public TrajetoAereoRelatorioDTO getVolta() {
        return agregarTrajetos(SentidoVoo.VOLTA);
    }

    private TrajetoAereoRelatorioDTO agregarTrajetos(SentidoVoo sentido) {
        List<TrajetoAereoRelatorioDTO> trajetos = trajetosAereos.stream()
                .filter(Objects::nonNull)
                .filter(trajeto -> sentido.name().equals(trajeto.getSentido()))
                .toList();
        if (trajetos.isEmpty()) {
            return null;
        }
        TrajetoAereoRelatorioDTO agregado = new TrajetoAereoRelatorioDTO();
        agregado.setSentido(sentido.name());
        agregado.setReservas(trajetos.stream().flatMap(trajeto -> trajeto.getReservas().stream()).toList());
        agregado.setValorTotal(trajetos.stream().map(TrajetoAereoRelatorioDTO::getValorTotal)
                .filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        return agregado;
    }

    public String getNomesViajantes() {
        return viajantes.stream().map(ViajanteRelatorioDTO::getNome)
                .filter(Objects::nonNull).collect(Collectors.joining(" • "));
    }

    public int getQuantidadeViajantes() {
        return viajantes.size();
    }

    public String getPeriodo() {
        if (inicio == null && fim == null) {
            return "";
        }
        return (inicio == null ? "" : inicio.toString()) + " a " + (fim == null ? "" : fim.toString());
    }

    public TransferRelatorioDTO getTransfer() {
        return transfers.isEmpty() ? null : new TransferRelatorioDTO(transfers);
    }

    public AluguelVeiculoRelatorioDTO getAluguelCarro() {
        return alugueis.isEmpty() ? null : new AluguelVeiculoRelatorioDTO(alugueis);
    }

    public String getOrientacoes() {
        return observacoes;
    }
}
