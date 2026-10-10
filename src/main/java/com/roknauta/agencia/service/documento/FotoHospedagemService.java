package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.FotoHospedagem;
import com.roknauta.agencia.dto.documento.FotoHospedagemDTO;
import com.roknauta.agencia.repository.documento.FotoHospedagemRepository;
import com.roknauta.agencia.service.mapper.FotoHospedagemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.roknauta.agencia.domain.documento.Hospedagem;
import com.roknauta.agencia.repository.documento.HospedagemRepository;

@Service
public class FotoHospedagemService {
    private final FotoHospedagemRepository repository;
    private final FotoHospedagemMapper mapper;
    private final HospedagemRepository hospedagemRepository;

    public FotoHospedagemService(FotoHospedagemRepository repository, FotoHospedagemMapper mapper,
                           HospedagemRepository hospedagemRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.hospedagemRepository = hospedagemRepository;
    }

    @Transactional(readOnly = true)
    public FotoHospedagemDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("FotoHospedagem não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<FotoHospedagemDTO> buscar(FotoHospedagemDTO filtro) {
        Long hospedagemId = filtro.getHospedagem() == null ? null : filtro.getHospedagem().getId();
        return repository.buscar(hospedagemId).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public FotoHospedagemDTO salvar(FotoHospedagemDTO dto) {
        FotoHospedagem entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("FotoHospedagem não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        entidade.setHospedagem(hospedagemReference(dto));
        return mapper.toDTO(repository.save(entidade));
    }

    private Hospedagem hospedagemReference(FotoHospedagemDTO dto) {
        if (dto.getHospedagem() == null || dto.getHospedagem().getId() == null) {
            throw new IllegalArgumentException("Hospedagem é obrigatória.");
        }
        return hospedagemRepository.getReferenceById(dto.getHospedagem().getId());
    }
}
