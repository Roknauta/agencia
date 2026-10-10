package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.BagagemReserva;
import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.dto.documento.BagagemReservaDTO;
import com.roknauta.agencia.repository.BagagemCompanhiaAereaRepository;
import com.roknauta.agencia.repository.documento.BagagemReservaRepository;
import com.roknauta.agencia.service.mapper.BagagemReservaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class BagagemReservaService {
    private final BagagemReservaRepository repository;
    private final BagagemCompanhiaAereaRepository bagagemRepository;
    private final BagagemReservaMapper mapper;

    public BagagemReservaService(BagagemReservaRepository repository,
                                 BagagemCompanhiaAereaRepository bagagemRepository,
                                 BagagemReservaMapper mapper) {
        this.repository = repository;
        this.bagagemRepository = bagagemRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public BagagemReservaDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("BagagemReserva não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<BagagemReservaDTO> buscar(BagagemReservaDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getDescricao()));
        Long bagagemId = filtro.getBagagemCompanhiaAerea() == null
                ? null : filtro.getBagagemCompanhiaAerea().getId();
        return repository.buscar(texto, bagagemId).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public BagagemReservaDTO salvar(BagagemReservaDTO dto) {
        BagagemReserva entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("BagagemReserva não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        if (dto.getBagagemCompanhiaAerea() == null || dto.getBagagemCompanhiaAerea().getId() == null) {
            throw new IllegalArgumentException("Franquia de bagagem da companhia é obrigatória.");
        }
        BagagemCompanhiaAerea bagagem = bagagemRepository.getReferenceById(
                dto.getBagagemCompanhiaAerea().getId());
        entidade.setBagagemCompanhiaAerea(bagagem);
        return mapper.toDTO(repository.save(entidade));
    }
}
