package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.repository.documento.ViajanteRepository;
import com.roknauta.agencia.service.mapper.ViajanteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ViajanteService {
    private final ViajanteRepository repository;
    private final ViajanteMapper mapper;

    public ViajanteService(ViajanteRepository repository, ViajanteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public ViajanteDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Viajante não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<ViajanteDTO> buscar(ViajanteDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNome()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public ViajanteDTO salvar(ViajanteDTO dto) {
        Viajante entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Viajante não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
