package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.repository.documento.TrajetoAereoRepository;
import com.roknauta.agencia.service.mapper.TrajetoAereoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrajetoAereoService {
    private final TrajetoAereoRepository repository;
    private final TrajetoAereoMapper mapper;
    private final TrechoReservaService trechoReservaService;

    public TrajetoAereoService(TrajetoAereoRepository repository, TrajetoAereoMapper mapper,
                               TrechoReservaService trechoReservaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.trechoReservaService = trechoReservaService;
    }

    @Transactional(readOnly = true)
    public TrajetoAereoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("TrajetoAereo não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TrajetoAereoDTO> buscar(TrajetoAereoDTO filtro) {
        var sentido = filtro.getSentido();
        return repository.buscar(sentido).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TrajetoAereoDTO salvar(TrajetoAereoDTO dto) {
        TrajetoAereo entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("TrajetoAereo não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        trechoReservaService.resolverReferencias(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }
}
