package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TipoTaxa;
import com.roknauta.agencia.dto.documento.TipoTaxaDTO;
import com.roknauta.agencia.repository.documento.TaxaRepository;
import com.roknauta.agencia.repository.documento.TipoTaxaRepository;
import com.roknauta.agencia.service.crud.CrudServiceBase;
import com.roknauta.agencia.service.mapper.TipoTaxaMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoTaxaService extends CrudServiceBase<TipoTaxa, TipoTaxaDTO> {

    private final TipoTaxaRepository repository;
    private final TaxaRepository taxaRepository;
    private final TipoTaxaMapper mapper;

    public TipoTaxaService(TipoTaxaRepository repository, TaxaRepository taxaRepository, TipoTaxaMapper mapper) {
        this.repository = repository;
        this.taxaRepository = taxaRepository;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<TipoTaxa, Long> getRepository() {
        return repository;
    }

    @Override
    protected TipoTaxaDTO toDTO(TipoTaxa entity) {
        return mapper.toDTO(entity);
    }

    @Override
    protected TipoTaxa toEntity(TipoTaxaDTO dto) {
        validarNome(dto);
        return mapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(TipoTaxaDTO dto, TipoTaxa entity) {
        validarNome(dto);
        mapper.updateEntity(dto, entity);
    }

    @Override
    public List<TipoTaxaDTO> find(TipoTaxaDTO filtro) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNome()));
        Boolean ativo = filtro.getAtivo();
        return repository.buscar(nome, ativo).stream().map(mapper::toDTO).toList();
    }

    public List<TipoTaxaDTO> findAtivosByTexto(String texto) {
        String nome = StringUtils.defaultString(StringUtils.trimToNull(texto));
        return repository.buscar(nome, true).stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (taxaRepository.existsByTipoTaxaId(id)) {
            throw new IllegalStateException("Tipo de taxa utilizado não pode ser excluído; desative-o.");
        }
        super.delete(id);
    }

    private void validarNome(TipoTaxaDTO dto) {
        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do tipo de taxa.");
        }
        boolean duplicado = dto.getId() == null ? repository.existsByNomeIgnoreCase(dto.getNome().trim())
            : repository.existsByNomeIgnoreCaseAndIdNot(dto.getNome().trim(), dto.getId());
        if (duplicado) {
            throw new IllegalArgumentException("Já existe um tipo de taxa com esse nome.");
        }
        dto.setNome(dto.getNome().trim());
        if (dto.getAtivo() == null) {
            dto.setAtivo(true);
        }
    }
}
