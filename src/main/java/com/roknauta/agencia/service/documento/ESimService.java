package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.ESim;
import com.roknauta.agencia.dto.documento.ESimDTO;
import com.roknauta.agencia.repository.documento.ESimRepository;
import com.roknauta.agencia.service.mapper.ESimMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ESimService {

    private final ESimRepository repository;
    private final ESimMapper mapper;

    public ESimService(ESimRepository repository, ESimMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ESimDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("ESim não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<ESimDTO> buscar(ESimDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getOperadora()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public ESimDTO salvar(ESimDTO dto) {
        ESim entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("ESim não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
