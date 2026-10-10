package com.roknauta.agencia.service.documento;

import java.time.LocalDateTime;
import java.util.List;

import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.Agencia;
import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.repository.AgenciaRepository;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.mapper.AgenciaMapper;
import com.roknauta.agencia.service.mapper.ContratoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ContratoService {
    private final ContratoRepository repository;
    private final OrcamentoRepository orcamentoRepository;
    private final ContratoMapper mapper;
    private final TrechoReservaService trechoReservaService;
    private final AgenciaRepository agenciaRepository;
    private final AgenciaMapper agenciaMapper;

    public ContratoService(ContratoRepository repository, OrcamentoRepository orcamentoRepository,
                           ContratoMapper mapper, TrechoReservaService trechoReservaService,
                           AgenciaRepository agenciaRepository, AgenciaMapper agenciaMapper) {
        this.repository = repository;
        this.orcamentoRepository = orcamentoRepository;
        this.mapper = mapper;
        this.trechoReservaService = trechoReservaService;
        this.agenciaRepository = agenciaRepository;
        this.agenciaMapper = agenciaMapper;
    }

    @Transactional(readOnly = true)
    public ContratoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<ContratoDTO> buscar(ContratoDTO filtro) {
        String numero = StringUtils.defaultString(StringUtils.trimToNull(filtro.getNumero()));
        String cliente = StringUtils.defaultString(StringUtils.trimToNull(filtro.getCliente()));
        Long orcamentoId = filtro.getOrcamento() == null ? null : filtro.getOrcamento().getId();
        return repository.buscar(numero, cliente, orcamentoId).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public ContratoDTO salvar(ContratoDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException("Contrato é imutável; crie um novo registro.");
        }
        entidadeOrcamentoObrigatorio(dto);
        dto.setNumero(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now())
                + "-" + UUID.randomUUID().toString().substring(0, 8));
        Contrato contrato = mapper.toEntity(dto);
        contrato.setOrcamento(orcamentoRepository.getReferenceById(dto.getOrcamento().getId()));
        Agencia agencia = agenciaRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException(
                        "Cadastre os dados da agência antes de emitir um contrato."));
        contrato.setDadosAgencia(agenciaMapper.toSnapshot(agencia));
        trechoReservaService.resolverReferencias(dto, contrato);
        return mapper.toDTO(repository.save(contrato));
    }

    private void entidadeOrcamentoObrigatorio(ContratoDTO dto) {
        if (dto.getOrcamento() == null || dto.getOrcamento().getId() == null) {
            throw new IllegalArgumentException("Orçamento aprovado é obrigatório para criar o contrato.");
        }
    }
}
