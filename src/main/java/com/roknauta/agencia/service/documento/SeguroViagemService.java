package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.SeguroViagem;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.repository.documento.SeguroViagemRepository;
import com.roknauta.agencia.service.mapper.SeguroViagemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class SeguroViagemService {
    private final SeguroViagemRepository repository;
    private final SeguroViagemMapper mapper;

    public SeguroViagemService(SeguroViagemRepository repository, SeguroViagemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public SeguroViagemDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("SeguroViagem não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<SeguroViagemDTO> buscar(SeguroViagemDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getSeguradora()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public SeguroViagemDTO salvar(SeguroViagemDTO dto) {
        SeguroViagem entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("SeguroViagem não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
