package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.TarifaAerea;
import com.roknauta.agencia.domain.documento.TrechoReserva;
import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.domain.documento.DocumentoViagem;
import com.roknauta.agencia.domain.documento.Reserva;
import com.roknauta.agencia.domain.documento.Voo;
import com.roknauta.agencia.dto.documento.DocumentoViagemDTO;
import com.roknauta.agencia.dto.documento.ReservaDTO;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.dto.documento.TrechoReservaDTO;
import com.roknauta.agencia.repository.TarifaAereaRepository;
import com.roknauta.agencia.repository.documento.TrechoReservaRepository;
import com.roknauta.agencia.repository.documento.VooRepository;
import com.roknauta.agencia.service.mapper.TrechoReservaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrechoReservaService {

    private final TrechoReservaRepository repository;
    private final VooRepository vooRepository;
    private final TarifaAereaRepository tarifaRepository;
    private final TrechoReservaMapper mapper;

    public TrechoReservaService(TrechoReservaRepository repository,
                                VooRepository vooRepository,
                                TarifaAereaRepository tarifaRepository,
                                TrechoReservaMapper mapper) {
        this.repository = repository;
        this.vooRepository = vooRepository;
        this.tarifaRepository = tarifaRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public TrechoReservaDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Trecho da reserva não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TrechoReservaDTO> buscar(TrechoReservaDTO filtro) {
        String numeroVoo = filtro.getVoo() == null
                ? "" : StringUtils.defaultString(StringUtils.trimToNull(filtro.getVoo().getNumero()));
        Long tarifaId = filtro.getTarifaAerea() == null ? null : filtro.getTarifaAerea().getId();
        return repository.buscar(numeroVoo, filtro.getDataPartida(), tarifaId)
                .stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TrechoReservaDTO salvar(TrechoReservaDTO dto) {
        TrechoReserva trecho;
        if (dto.getId() == null) {
            trecho = mapper.toEntity(dto);
        } else {
            trecho = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Trecho da reserva não encontrado."));
            mapper.updateEntity(dto, trecho);
        }
        setReferences(dto, trecho);
        return mapper.toDTO(repository.save(trecho));
    }

    public void resolverReferencias(ReservaDTO dto, Reserva reserva) {
        if (dto.getTrechos() == null || dto.getTrechos().isEmpty()) {
            return;
        }
        if (reserva.getTrechos() == null || reserva.getTrechos().size() != dto.getTrechos().size()) {
            throw new IllegalArgumentException("A lista de trechos da reserva está inconsistente.");
        }
        for (int i = 0; i < dto.getTrechos().size(); i++) {
            setReferences(dto.getTrechos().get(i), reserva.getTrechos().get(i));
        }
    }

    public void resolverReferencias(TrajetoAereoDTO dto, TrajetoAereo trajeto) {
        if (dto.getReservas() == null || dto.getReservas().isEmpty()) {
            return;
        }
        if (trajeto.getReservas() == null || trajeto.getReservas().size() != dto.getReservas().size()) {
            throw new IllegalArgumentException("A lista de reservas do trajeto aéreo está inconsistente.");
        }
        for (int i = 0; i < dto.getReservas().size(); i++) {
            resolverReferencias(dto.getReservas().get(i), trajeto.getReservas().get(i));
        }
    }

    public void resolverReferencias(DocumentoViagemDTO dto, DocumentoViagem documento) {
        if (dto.getTrajetosAereos() == null || dto.getTrajetosAereos().isEmpty()) {
            return;
        }
        if (documento.getTrajetosAereos() == null
                || documento.getTrajetosAereos().size() != dto.getTrajetosAereos().size()) {
            throw new IllegalArgumentException("A lista de trajetos aéreos está inconsistente.");
        }
        for (int i = 0; i < dto.getTrajetosAereos().size(); i++) {
            resolverReferencias(dto.getTrajetosAereos().get(i), documento.getTrajetosAereos().get(i));
        }
    }

    private void setReferences(TrechoReservaDTO dto, TrechoReserva trecho) {
        if (dto.getVoo() == null || dto.getVoo().getId() == null
                || dto.getDataPartida() == null || dto.getDataChegada() == null
                || dto.getTarifaAerea() == null || dto.getTarifaAerea().getId() == null) {
            throw new IllegalArgumentException("Voo, datas e tarifa são obrigatórios no trecho da reserva.");
        }

        Voo voo = vooRepository.findById(dto.getVoo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Voo não encontrado."));
        TarifaAerea tarifa = tarifaRepository.findById(dto.getTarifaAerea().getId())
                .orElseThrow(() -> new IllegalArgumentException("Tarifa aérea não encontrada."));
        if (!tarifa.getCompanhiaAerea().getId().equals(voo.getCompanhiaAerea().getId())) {
            throw new IllegalArgumentException("A tarifa deve pertencer à companhia aérea do voo.");
        }

        trecho.setVoo(voo);
        trecho.setTarifaAerea(tarifa);
    }
}
