package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.repository.documento.TrajetoAereoRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.TrajetoAereoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrajetoAereoService implements CrudService<TrajetoAereoDTO> {

    private final TrajetoAereoRepository repository;
    private final TrajetoAereoMapper mapper;
    private final TrechoAereoService trechoAereoService;
    private final OrcamentoRepository orcamentoRepository;
    private final ServicoTaxaService servicoTaxaService;

    public TrajetoAereoService(TrajetoAereoRepository repository, TrajetoAereoMapper mapper,
        TrechoAereoService trechoAereoService, OrcamentoRepository orcamentoRepository,
        ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.trechoAereoService = trechoAereoService;
        this.orcamentoRepository = orcamentoRepository;
        this.servicoTaxaService = servicoTaxaService;
    }

    @Transactional(readOnly = true)
    public TrajetoAereoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("TrajetoAereo não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TrajetoAereoDTO> buscar(TrajetoAereoDTO filtro) {
        var sentido = filtro.getSentido();
        return repository.buscar(sentido).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TrajetoAereoDTO salvar(TrajetoAereoDTO dto) {
        TrajetoAereo entidade;
        if (dto.getId() == null) {
            entidade = mapper.toEntity(dto);
        } else {
            entidade = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("TrajetoAereo não encontrado."));
            mapper.updateEntity(dto, entidade);
        }
        trechoAereoService.resolverReferencias(dto, entidade);
        servicoTaxaService.resolver(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    @Override
    @Transactional(readOnly = true)
    public TrajetoAereoDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrajetoAereoDTO> find(TrajetoAereoDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrajetoAereoDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public TrajetoAereoDTO save(TrajetoAereoDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id))
            throw new IllegalArgumentException("Trajeto aéreo não encontrado.");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TrajetoAereoDTO> findByOrcamento(Long idOrcamento) {
        return orcamentoRepository.findById(idOrcamento)
            .map(orcamento -> orcamento.getTrajetosAereos().stream().map(mapper::toDTO).toList()).orElse(List.of());
    }

    @Transactional(readOnly = true)
    public Long findIdByTrajetoAereo(Long id) {
        return orcamentoRepository.findByTrajetoAereoId(id).map(orcamento -> orcamento.getId())
            .orElseThrow(() -> new IllegalArgumentException("Trajeto aéreo não está vinculado a um orçamento."));
    }

    @Transactional
    public TrajetoAereoDTO salvarNoOrcamento(Long idOrcamento, TrajetoAereoDTO dto) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        servicoTaxaService.validarMoedaOrcamento(List.of(dto), orcamento.getMoeda());
        TrajetoAereo trajeto;
        if (dto.getId() == null) {
            trajeto = mapper.toEntity(dto);
            orcamento.getTrajetosAereos().add(trajeto);
        } else {
            trajeto = orcamento.getTrajetosAereos().stream().filter(item -> dto.getId().equals(item.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Trajeto não pertence ao orçamento."));
            mapper.updateEntity(dto, trajeto);
        }
        trechoAereoService.resolverReferencias(dto, trajeto);
        servicoTaxaService.resolver(dto, trajeto);
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(trajeto);
    }

    @Transactional
    public void excluirDoOrcamento(Long idOrcamento, Long idTrajeto) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        if (!orcamento.getTrajetosAereos().removeIf(item -> idTrajeto.equals(item.getId())))
            throw new IllegalArgumentException("Trajeto não pertence ao orçamento.");
        orcamentoRepository.save(orcamento);
    }
}
