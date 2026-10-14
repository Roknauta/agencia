package com.roknauta.agencia.service.documento;

import java.util.List;
import java.util.ArrayList;

import com.roknauta.agencia.domain.documento.SeguroViagem;
import com.roknauta.agencia.domain.documento.Viajante;
import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.dto.documento.SeguroViagemDTO;
import com.roknauta.agencia.dto.documento.ViajanteDTO;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.repository.documento.SeguroViagemRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.SeguroViagemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class SeguroViagemService implements CrudService<SeguroViagemDTO> {

    private final SeguroViagemRepository repository;
    private final SeguroViagemMapper mapper;
    private final OrcamentoRepository orcamentoRepository;
    private final ServicoTaxaService servicoTaxaService;

    public SeguroViagemService(SeguroViagemRepository repository, SeguroViagemMapper mapper,
        OrcamentoRepository orcamentoRepository, ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.orcamentoRepository = orcamentoRepository;
        this.servicoTaxaService = servicoTaxaService;
    }

    @Transactional(readOnly = true)
    public SeguroViagemDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("SeguroViagem não encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public SeguroViagemDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public List<SeguroViagemDTO> buscar(SeguroViagemDTO filtro) {
        String texto = StringUtils.defaultString(StringUtils.trimToNull(filtro.getSeguradora()));
        return repository.buscar(texto).stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeguroViagemDTO> find(SeguroViagemDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeguroViagemDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public SeguroViagemDTO save(SeguroViagemDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Seguro viagem não encontrado.");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Long findIdBySeguroViagem(Long id) {
        return orcamentoRepository.findBySeguroViagemId(id).map(orcamento -> orcamento.getId())
            .orElseThrow(() -> new IllegalArgumentException("Seguro viagem não está vinculado a um orçamento."));
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
        servicoTaxaService.resolver(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    @Transactional(readOnly = true)
    public List<SeguroViagemDTO> findByOrcamento(Long idOrcamento) {
        return orcamentoRepository.findById(idOrcamento)
            .map(orcamento -> orcamento.getSegurosViagem().stream().map(mapper::toDTO).toList()).orElse(List.of());
    }

    @Transactional
    public SeguroViagemDTO salvarNoOrcamento(Long idOrcamento, SeguroViagemDTO dto) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        servicoTaxaService.validarMoedaOrcamento(List.of(dto), orcamento.getMoeda());
        SeguroViagem seguro;
        if (dto.getId() == null) {
            seguro = mapper.toEntity(dto);
            orcamento.getSegurosViagem().add(seguro);
        } else {
            seguro = orcamento.getSegurosViagem().stream().filter(item -> dto.getId().equals(item.getId())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Seguro viagem não pertence ao orçamento."));
            mapper.updateEntity(dto, seguro);
        }
        if (dto.getInicioVigencia() != null && dto.getFimVigencia() != null
            && dto.getFimVigencia().isBefore(dto.getInicioVigencia())) {
            throw new IllegalArgumentException("Fim da vigência deve ser posterior ao início.");
        }
        resolverViajantes(dto, seguro, orcamento);
        servicoTaxaService.resolver(dto, seguro);
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(seguro);
    }

    @Transactional
    public void excluirDoOrcamento(Long idOrcamento, Long idSeguro) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        if (!orcamento.getSegurosViagem().removeIf(item -> idSeguro.equals(item.getId()))) {
            throw new IllegalArgumentException("Seguro viagem não pertence ao orçamento.");
        }
        orcamentoRepository.save(orcamento);
    }

    private void resolverViajantes(SeguroViagemDTO dto, SeguroViagem seguro, Orcamento orcamento) {
        List<Viajante> viajantes = new ArrayList<>();
        if (dto.getViajantesCobertos() != null) {
            for (ViajanteDTO viajanteDTO : dto.getViajantesCobertos()) {
                if (viajanteDTO == null || viajanteDTO.getId() == null) {
                    throw new IllegalArgumentException("Selecione viajantes válidos do orçamento.");
                }
                Viajante viajante = orcamento.getViajantes().stream()
                    .filter(item -> viajanteDTO.getId().equals(item.getId())).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Selecione viajantes válidos do orçamento."));
                viajantes.add(viajante);
            }
        }
        seguro.setViajantesCobertos(viajantes);
    }
}
