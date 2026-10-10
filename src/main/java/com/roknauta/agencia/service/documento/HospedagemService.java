package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Hospedagem;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import com.roknauta.agencia.repository.documento.HospedagemRepository;
import com.roknauta.agencia.service.mapper.HospedagemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class HospedagemService {
    private final HospedagemRepository repository;
    private final HospedagemMapper mapper;

    public HospedagemService(HospedagemRepository repository, HospedagemMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public HospedagemDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Hospedagem não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<HospedagemDTO> buscar(HospedagemDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getLocalizador()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public HospedagemDTO salvar(HospedagemDTO dto) {
        Hospedagem entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Hospedagem não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        return mapper.toDTO(repository.save(entidade));
    }
}
