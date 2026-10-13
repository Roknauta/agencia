package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Transfer;
import com.roknauta.agencia.dto.documento.TransferDTO;
import com.roknauta.agencia.repository.documento.TransferRepository;
import com.roknauta.agencia.service.mapper.TransferMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class TransferService {

    private final TransferRepository repository;
    private final TransferMapper mapper;

    public TransferService(TransferRepository repository, TransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public TransferDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Transfer não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TransferDTO> buscar(TransferDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getPrestador()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TransferDTO salvar(TransferDTO dto) {
        Transfer entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Transfer não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
