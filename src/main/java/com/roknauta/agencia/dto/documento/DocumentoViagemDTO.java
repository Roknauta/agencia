package com.roknauta.agencia.dto.documento;

import com.roknauta.agencia.dto.CidadeDTO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.domain.documento.Moeda;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class DocumentoViagemDTO extends BaseDTO {
    private String numero;
    private String cliente;
    private String consultor;
    private LocalDate dataDocumento;
    private Moeda moeda = Moeda.BRL;
    private List<TransferDTO> transfers = new ArrayList<>();
    private List<HospedagemDTO> hospedagens = new ArrayList<>();
    private List<AluguelVeiculoDTO> alugueis = new ArrayList<>();
    private List<SeguroViagemDTO> segurosViagem = new ArrayList<>();
    private List<ESimDTO> eSims = new ArrayList<>();
    private CidadeDTO origem;
    private String destino;
    private LocalDate inicio;
    private LocalDate fim;
    private String observacoes;
    private List<ViajanteDTO> viajantes = new ArrayList<>();
    private List<TrajetoAereoDTO> trajetosAereos = new ArrayList<>();
    private List<TrajetoRodoviarioDTO> trajetosRodoviarios = new ArrayList<>();
}
