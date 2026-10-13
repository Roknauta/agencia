package com.roknauta.agencia.service.documento;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.roknauta.agencia.dto.AgenciaDTO;
import com.roknauta.agencia.domain.documento.CanalAceiteContrato;
import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.Orcamento;
import com.roknauta.agencia.domain.documento.StatusContrato;
import com.roknauta.agencia.domain.documento.StatusOrcamento;
import com.roknauta.agencia.dto.documento.ContratoDTO;
import com.roknauta.agencia.repository.documento.ContratoRepository;
import com.roknauta.agencia.repository.documento.OrcamentoRepository;
import com.roknauta.agencia.service.AgenciaService;
import com.roknauta.agencia.service.mapper.ContratoMapper;
import com.roknauta.agencia.web.jasper.JasperUtils;
import com.roknauta.agencia.web.jasper.documento.contrato.ContratoRelatorioDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

@Service
public class ContratoService {

    private final ContratoRepository repository;
    private final OrcamentoRepository orcamentoRepository;
    private final ContratoMapper mapper;
    private final TrechoAereoService trechoAereoService;
    private final AgenciaService agenciaService;

    public ContratoService(ContratoRepository repository, OrcamentoRepository orcamentoRepository,
        ContratoMapper mapper, TrechoAereoService trechoAereoService, AgenciaService agenciaService) {
        this.repository = repository;
        this.orcamentoRepository = orcamentoRepository;
        this.mapper = mapper;
        this.trechoAereoService = trechoAereoService;
        this.agenciaService = agenciaService;
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

    @Transactional(readOnly = true)
    public byte[] gerarPdf(Long id, Map<String, Object> parametros) throws IOException, JRException {
        ContratoDTO contrato = buscarPorId(id);
        AgenciaDTO agencia = agenciaService.configuracao();
        return JasperUtils.gerarContratoPdf(new ContratoRelatorioDTO(contrato, agencia), parametros);
    }

    @Transactional
    public ContratoDTO salvar(ContratoDTO dto) {
        if (dto.getId() != null) {
            throw new UnsupportedOperationException("Contrato é imutável; crie um novo registro.");
        }
        entidadeOrcamentoObrigatorio(dto);
        Orcamento orcamento = orcamentoRepository.findById(dto.getOrcamento().getId())
            .orElseThrow(() -> new IllegalArgumentException("Orçamento não encontrado."));
        if (orcamento.getStatus() != StatusOrcamento.CLIENTE_INTERESSADO) {
            throw new IllegalStateException("O contrato exige um orçamento com interesse do cliente registrado.");
        }
        if (repository.existsByOrcamentoId(orcamento.getId())) {
            throw new IllegalStateException("Este orçamento já está vinculado a um contrato.");
        }
        validarConsultaEPreReserva(dto);
        dto.setNumero(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "-"
            + UUID.randomUUID().toString().substring(0, 8));
        Contrato contrato = mapper.toEntity(dto);
        contrato.setOrcamento(orcamento);
        contrato.setStatus(StatusContrato.AGUARDANDO_ACEITE);
        trechoAereoService.resolverReferencias(dto, contrato);
        Contrato salvo = repository.save(contrato);
        orcamento.setStatus(StatusOrcamento.CONVERTIDO_EM_CONTRATO);
        orcamentoRepository.save(orcamento);
        return mapper.toDTO(salvo);
    }

    @Transactional
    public ContratoDTO registrarAceite(Long id, CanalAceiteContrato canal, String referenciaAceite) {
        Contrato contrato = buscarEntidade(id);
        if (contrato.getStatus() != StatusContrato.AGUARDANDO_ACEITE) {
            throw new IllegalStateException("O contrato não está aguardando aceite.");
        }
        validarPreReservaVigente(contrato);
        if (canal == null || StringUtils.isBlank(referenciaAceite)) {
            throw new IllegalArgumentException("Canal e referência do aceite são obrigatórios.");
        }
        contrato.setStatus(StatusContrato.ACEITO_AGUARDANDO_PAGAMENTO);
        contrato.setDataAceite(Instant.now());
        contrato.setCanalAceite(canal);
        contrato.setReferenciaAceite(referenciaAceite.trim());
        return mapper.toDTO(repository.save(contrato));
    }

    @Transactional
    public ContratoDTO confirmarPagamento(Long id) {
        Contrato contrato = buscarEntidade(id);
        if (contrato.getStatus() != StatusContrato.ACEITO_AGUARDANDO_PAGAMENTO) {
            throw new IllegalStateException("O contrato ainda não foi aceito ou já teve o pagamento confirmado.");
        }
        validarPreReservaVigente(contrato);
        contrato.setStatus(StatusContrato.PAGAMENTO_CONFIRMADO);
        contrato.setDataPagamentoConfirmado(Instant.now());
        return mapper.toDTO(repository.save(contrato));
    }

    private Contrato buscarEntidade(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado."));
    }

    private void validarConsultaEPreReserva(ContratoDTO dto) {
        if (dto.getDataConsultaPrecos() == null || StringUtils.isBlank(dto.getReferenciaPreReserva())
            || dto.getPreReservaValidaAte() == null) {
            throw new IllegalArgumentException(
                "Data da consulta, referência e validade da pré-reserva são obrigatórias para gerar o contrato.");
        }
        if (!dto.getPreReservaValidaAte().isAfter(Instant.now())) {
            throw new IllegalArgumentException("A pré-reserva precisa estar vigente para gerar o contrato.");
        }
    }

    private void validarPreReservaVigente(Contrato contrato) {
        if (contrato.getPreReservaValidaAte() == null || !contrato.getPreReservaValidaAte().isAfter(Instant.now())) {
            throw new IllegalStateException("O prazo da pré-reserva expirou.");
        }
    }

    private void entidadeOrcamentoObrigatorio(ContratoDTO dto) {
        if (dto.getOrcamento() == null || dto.getOrcamento().getId() == null) {
            throw new IllegalArgumentException("Orçamento aprovado é obrigatório para criar o contrato.");
        }
    }
}
