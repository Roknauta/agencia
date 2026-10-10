package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Reserva;
import com.roknauta.agencia.dto.documento.ReservaDTO;
import com.roknauta.agencia.repository.documento.ReservaRepository;
import com.roknauta.agencia.service.mapper.ReservaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ReservaService {
    private final ReservaRepository repository;
    private final ReservaMapper mapper;
    private final TrechoReservaService trechoReservaService;

    public ReservaService(ReservaRepository repository, ReservaMapper mapper,
                          TrechoReservaService trechoReservaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.trechoReservaService = trechoReservaService;
    }

    @Transactional(readOnly = true)
    public ReservaDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> buscar(ReservaDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getLocalizador()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public ReservaDTO salvar(ReservaDTO dto) {
        Reserva entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        trechoReservaService.resolverReferencias(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }
}
