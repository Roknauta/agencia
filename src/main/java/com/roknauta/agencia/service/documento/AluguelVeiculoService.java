package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.AluguelVeiculo;
import com.roknauta.agencia.dto.documento.AluguelVeiculoDTO;
import com.roknauta.agencia.repository.documento.AluguelVeiculoRepository;
import com.roknauta.agencia.service.mapper.AluguelVeiculoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class AluguelVeiculoService {
    private final AluguelVeiculoRepository repository;
    private final AluguelVeiculoMapper mapper;

    public AluguelVeiculoService(AluguelVeiculoRepository repository, AluguelVeiculoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public AluguelVeiculoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("AluguelVeiculo não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<AluguelVeiculoDTO> buscar(AluguelVeiculoDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getLocalizador()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public AluguelVeiculoDTO salvar(AluguelVeiculoDTO dto) {
        AluguelVeiculo entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("AluguelVeiculo não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
