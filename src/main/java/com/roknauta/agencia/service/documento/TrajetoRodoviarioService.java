package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TrajetoRodoviario;
import com.roknauta.agencia.dto.documento.TrajetoRodoviarioDTO;
import com.roknauta.agencia.repository.documento.TrajetoRodoviarioRepository;
import com.roknauta.agencia.service.mapper.TrajetoRodoviarioMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class TrajetoRodoviarioService {

    private final TrajetoRodoviarioRepository repository;
    private final TrajetoRodoviarioMapper mapper;

    public TrajetoRodoviarioService(TrajetoRodoviarioRepository repository, TrajetoRodoviarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public TrajetoRodoviarioDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("TrajetoRodoviario não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TrajetoRodoviarioDTO> buscar(TrajetoRodoviarioDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getTransportadora()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TrajetoRodoviarioDTO salvar(TrajetoRodoviarioDTO dto) {
        TrajetoRodoviario entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("TrajetoRodoviario não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
