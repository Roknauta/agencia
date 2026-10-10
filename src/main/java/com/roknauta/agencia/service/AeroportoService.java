package com.roknauta.agencia.service;

import com.roknauta.agencia.domain.Aeroporto;
import com.roknauta.agencia.domain.Cidade;
import com.roknauta.agencia.dto.AeroportoDTO;
import com.roknauta.agencia.repository.AeroportoRepository;
import com.roknauta.agencia.repository.CidadeRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.AeroportoMapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AeroportoService extends CrudServiceBase<Aeroporto, AeroportoDTO> {

    private final AeroportoRepository repository;
    private final CidadeRepository cidadeRepository;
    private final AeroportoMapper mapper;

    public AeroportoService(AeroportoRepository repository, CidadeRepository cidadeRepository,
                            AeroportoMapper mapper) {
        this.repository = repository;
        this.cidadeRepository = cidadeRepository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<Aeroporto, Long> getRepository() {
        return repository;
    }

    @Override
    protected AeroportoDTO toDTO(Aeroporto entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Aeroporto toEntity(AeroportoDTO dto) {
        Aeroporto entity = mapper.toEntity(dto);
        entity.setCidadeAtendida(cidadeReference(dto));
        return entity;
    }

    @Override
    protected void updateEntity(AeroportoDTO dto, Aeroporto entity) {
        mapper.updateEntity(dto, entity);
        entity.setCidadeAtendida(cidadeReference(dto));
    }

    @Override
    public List<AeroportoDTO> find(AeroportoDTO dto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(dto.getNome()));
        Long cidadeId = dto.getCidadeAtendida() == null ? null : dto.getCidadeAtendida().getId();
        String codigoIata = StringUtils.defaultString(StringUtils.trimToNull(dto.getCodigoIata()));
        return repository.buscar(nome, cidadeId, codigoIata).stream().map(this::toDTO).toList();
    }

    public List<AeroportoDTO> findByNome(String nome) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(nome));
        return repository.buscar(filtro, null, "").stream().map(this::toDTO).toList();
    }

    private Cidade cidadeReference(AeroportoDTO dto) {
        if (dto.getCidadeAtendida() == null || dto.getCidadeAtendida().getId() == null) {
            throw new IllegalArgumentException("Cidade atendida é obrigatória.");
        }
        return cidadeRepository.getReferenceById(dto.getCidadeAtendida().getId());
    }
}
