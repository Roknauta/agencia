package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Hospedagem;
import com.roknauta.agencia.domain.Hotel;
import com.roknauta.agencia.dto.documento.HospedagemDTO;
import com.roknauta.agencia.repository.HotelRepository;
import com.roknauta.agencia.repository.documento.HospedagemRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.crud.CrudService;
import com.roknauta.agencia.service.mapper.HospedagemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class HospedagemService implements CrudService<HospedagemDTO> {

    private final HospedagemRepository repository;
    private final OrcamentoRepository orcamentoRepository;
    private final HotelRepository hotelRepository;
    private final HospedagemMapper mapper;
    private final ServicoTaxaService servicoTaxaService;

    public HospedagemService(HospedagemRepository repository, OrcamentoRepository orcamentoRepository,
        HotelRepository hotelRepository, HospedagemMapper mapper, ServicoTaxaService servicoTaxaService) {
        this.repository = repository;
        this.orcamentoRepository = orcamentoRepository;
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
        this.servicoTaxaService = servicoTaxaService;
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
        entidade.setHotelSelecionado(hotelReference(dto));
        servicoTaxaService.resolver(dto, entidade);
        return mapper.toDTO(repository.save(entidade));
    }

    @Override
    @Transactional(readOnly = true)
    public HospedagemDTO findById(Long id) {
        return buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospedagemDTO> find(HospedagemDTO filtro) {
        return buscar(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HospedagemDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    @Transactional
    public HospedagemDTO save(HospedagemDTO dto) {
        return salvar(dto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Hospedagem não encontrada.");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<HospedagemDTO> findByOrcamento(Long idOrcamento) {
        return orcamentoRepository.findById(idOrcamento)
            .map(orcamento -> orcamento.getHospedagens().stream().map(mapper::toDTO).toList()).orElse(List.of());
    }

    @Transactional
    public HospedagemDTO salvarNoOrcamento(Long idOrcamento, HospedagemDTO dto) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        servicoTaxaService.validarMoedaOrcamento(List.of(dto), orcamento.getMoeda());
        Hospedagem hospedagem;
        if (dto.getId() == null) {
            hospedagem = mapper.toEntity(dto);
            orcamento.getHospedagens().add(hospedagem);
        } else {
            hospedagem = orcamento.getHospedagens().stream().filter(item -> dto.getId().equals(item.getId()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Hospedagem não pertence ao orçamento."));
            mapper.updateEntity(dto, hospedagem);
        }
        hospedagem.setHotelSelecionado(hotelReference(dto));
        servicoTaxaService.resolver(dto, hospedagem);
        if (hospedagem.getFotos() != null) {
            hospedagem.getFotos().forEach(foto -> foto.setHospedagem(hospedagem));
        }
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(hospedagem);
    }

    @Transactional
    public void excluirDoOrcamento(Long idOrcamento, Long idHospedagem) {
        var orcamento = orcamentoRepository.findById(idOrcamento)
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        if (!orcamento.getHospedagens().removeIf(item -> idHospedagem.equals(item.getId()))) {
            throw new IllegalArgumentException("Hospedagem não pertence ao orçamento.");
        }
        orcamentoRepository.save(orcamento);
    }

    private Hotel hotelReference(HospedagemDTO dto) {
        if (dto.getHotelSelecionado() == null || dto.getHotelSelecionado().getId() == null) {
            throw new IllegalArgumentException("Hotel é obrigatório.");
        }
        return hotelRepository.getReferenceById(dto.getHotelSelecionado().getId());
    }
}
