package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.ESim;
import com.roknauta.agencia.dto.documento.ESimDTO;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.repository.documento.ESimRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.ESimMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ESimService implements CrudService<ESimDTO> {

    private final ESimRepository repository;
    private final ESimMapper mapper;
    private final OrcamentoRepository orcamentoRepository;
    private final ServicoTaxaService servicoTaxaService;

    public ESimService(ESimRepository repository, ESimMapper mapper, OrcamentoRepository orcamentoRepository,
        ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.orcamentoRepository = orcamentoRepository;
        this.servicoTaxaService = servicoTaxaService;
    }

    @Transactional(readOnly = true)
    public ESimDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("ESim não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public ESimDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public List<ESimDTO> buscar(ESimDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getOperadora()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ESimDTO> find(ESimDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ESimDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public ESimDTO save(ESimDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("eSIM não encontrado.");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long findIdByESim(Long id) {
        return orcamentoRepository.findByESimId(id).map(orcamento -> orcamento.getId())
            .orElseThrow(() -> new IllegalArgumentException("eSIM não está vinculado a um orçamento."));
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
        servicoTaxaService.resolver(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    @Transactional(readOnly = true)
    public List<ESimDTO> findByOrcamento(Long idOrcamento) {
        return orcamentoRepository.findById(idOrcamento)
            .map(orcamento -> orcamento.getESims().stream().map(mapper::toDTO).toList()).orElse(List.of());
    }

    @Transactional
    public ESimDTO salvarNoOrcamento(Long idOrcamento, ESimDTO dto) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        servicoTaxaService.validarMoedaOrcamento(List.of(dto), orcamento.getMoeda());
        ESim eSim;
        if (dto.getId() == null) {
            eSim = mapper.toEntity(dto);
            orcamento.getESims().add(eSim);
        } else {
            eSim = orcamento.getESims().stream().filter(item -> dto.getId().equals(item.getId())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("eSIM não pertence ao orçamento."));
            mapper.updateEntity(dto, eSim);
        }
        servicoTaxaService.resolver(dto, eSim);
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(eSim);
    }

    @Transactional
    public void excluirDoOrcamento(Long idOrcamento, Long idESim) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        if (!orcamento.getESims().removeIf(item -> idESim.equals(item.getId()))) {
            throw new IllegalArgumentException("eSIM não pertence ao orçamento.");
        }
        orcamentoRepository.save(orcamento);
    }
}
