package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.Aeroporto;
import com.roknauta.agencia.domain.CompanhiaAerea;
import com.roknauta.agencia.domain.documento.Voo;
import com.roknauta.agencia.dto.documento.VooDTO;
import com.roknauta.agencia.repository.AeroportoRepository;
import com.roknauta.agencia.repository.CompanhiaAereaRepository;
import com.roknauta.agencia.repository.documento.VooRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.VooMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VooService extends CrudServiceBase<Voo, VooDTO> {

    private final VooRepository repository;
    private final CompanhiaAereaRepository companhiaRepository;
    private final AeroportoRepository aeroportoRepository;
    private final VooMapper mapper;

    public VooService(VooRepository repository,
                      CompanhiaAereaRepository companhiaRepository,
                      AeroportoRepository aeroportoRepository,
                      VooMapper mapper) {
        this.repository = repository;
        this.companhiaRepository = companhiaRepository;
        this.aeroportoRepository = aeroportoRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public VooDTO save(VooDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException(
                    "Voo é imutável; cadastre uma nova versão.");
        }
        if (dto.getDataReferencia() == null) {
            throw new IllegalArgumentException("Data de referência é obrigatória.");
        }
        return super.save(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        throw new UnsupportedOperationException("Versões de voo não podem ser excluídas.");
    }

    @Override
    protected JpaRepository<Voo, Long> getRepository() {
        return repository;
    }

    @Override
    protected VooDTO toDTO(Voo entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected Voo toEntity(VooDTO dto) {
        Voo entity = mapper.toEntity(dto);
        setReferences(dto, entity);
        return entity;
    }

    @Override
    protected void updateEntity(VooDTO dto, Voo entity) {
        throw new UnsupportedOperationException(
                "Voo é imutável; cadastre uma nova versão.");
    }

    @Override
    public List<VooDTO> find(VooDTO dto) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(dto.getNumero()));
        Long companhiaId = dto.getCompanhiaAerea() == null ? null : dto.getCompanhiaAerea().getId();
        Long origemId = dto.getOrigem() == null ? null : dto.getOrigem().getId();
        Long destinoId = dto.getDestino() == null ? null : dto.getDestino().getId();
        return repository.buscar(numero, companhiaId, origemId, destinoId,
                        dto.getDataReferencia())
                .stream().map(this::toDTO).toList();
    }

    public List<VooDTO> findByNumeroCompanhiaAerea(String numero, Long idCompanhiaAerea) {
        String filtro = StringUtils.defaultString(StringUtils.trimToNull(numero));
        return repository.buscar(filtro, idCompanhiaAerea, null, null, null)
                .stream().map(this::toDTO).toList();
    }

    private void setReferences(VooDTO dto, Voo entity) {
        if (StringUtils.isBlank(dto.getNumero()) || dto.getDataReferencia() == null
                || dto.getCompanhiaAerea() == null || dto.getCompanhiaAerea().getId() == null
                || dto.getOrigem() == null || dto.getOrigem().getId() == null
                || dto.getDestino() == null || dto.getDestino().getId() == null
                || dto.getPartida() == null || dto.getChegada() == null) {
            throw new IllegalArgumentException("Preencha todos os campos obrigatórios do voo.");
        }

        CompanhiaAerea companhia = companhiaRepository.getReferenceById(dto.getCompanhiaAerea().getId());
        Aeroporto origem = aeroportoRepository.getReferenceById(dto.getOrigem().getId());
        Aeroporto destino = aeroportoRepository.getReferenceById(dto.getDestino().getId());

        entity.setCompanhiaAerea(companhia);
        entity.setOrigem(origem);
        entity.setDestino(destino);
    }
}
