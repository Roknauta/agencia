package com.roknauta.agencia.service.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.roknauta.agencia.domain.documento.Moeda;
import com.roknauta.agencia.domain.documento.Servico;
import com.roknauta.agencia.domain.documento.Taxa;
import com.roknauta.agencia.domain.documento.TipoTaxa;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import com.roknauta.agencia.repository.documento.TipoTaxaRepository;
import org.springframework.stereotype.Service;

@Service
public class ServicoTaxaService {

    private final TipoTaxaRepository tipoTaxaRepository;

    public ServicoTaxaService(TipoTaxaRepository tipoTaxaRepository) {
        this.tipoTaxaRepository = tipoTaxaRepository;
    }

    public void resolver(ServicoDTO dto, Servico servico) {
        validarValores(dto);
        List<Taxa> taxasAtuais = servico.getTaxas();
        if (taxasAtuais == null) {
            taxasAtuais = new ArrayList<>();
            servico.setTaxas(taxasAtuais);
        }
        List<Taxa> taxasResolvidas = new ArrayList<>();
        if (dto.getTaxas() != null) {
            for (int indice = 0; indice < dto.getTaxas().size(); indice++) {
                TaxaDTO taxaDTO = dto.getTaxas().get(indice);
                if (taxaDTO == null) {
                    continue;
                }
                if (taxaDTO.getTipoTaxa() == null || taxaDTO.getTipoTaxa().getId() == null) {
                    throw new IllegalArgumentException("Selecione o tipo de cada taxa do serviço.");
                }
                Moeda moedaTaxa = taxaDTO.getMoeda();
                if (moedaTaxa == null || moedaTaxa != dto.getMoeda()) {
                    throw new IllegalArgumentException("A moeda da taxa deve ser igual à moeda do serviço.");
                }
                if (taxaDTO.getValor() == null || taxaDTO.getValor().signum() < 0) {
                    throw new IllegalArgumentException("Informe um valor de taxa maior ou igual a zero.");
                }
                TipoTaxa tipoTaxa = tipoTaxaRepository.findById(taxaDTO.getTipoTaxa().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Tipo de taxa não encontrado."));
                boolean jaUsado = taxasAtuais.stream().anyMatch(atual -> Objects.equals(atual.getId(), taxaDTO.getId())
                    && atual.getTipoTaxa() != null && Objects.equals(atual.getTipoTaxa().getId(), tipoTaxa.getId()));
                if (!tipoTaxa.isAtivo() && !jaUsado) {
                    throw new IllegalArgumentException("Tipos de taxa inativos não podem ser adicionados a serviços.");
                }
                Taxa taxa = taxasAtuais.stream()
                    .filter(atual -> taxaDTO.getId() != null && Objects.equals(atual.getId(), taxaDTO.getId()))
                    .findFirst().orElseGet(Taxa::new);
                taxa.setTipoTaxa(tipoTaxa);
                taxa.setValor(taxaDTO.getValor());
                taxa.setMoeda(moedaTaxa);
                taxasResolvidas.add(taxa);
            }
        }
        servico.setValor(dto.getValor());
        servico.setDesconto(dto.getDesconto());
        servico.setMoeda(dto.getMoeda());
        servico.setRegrasAlteracao(dto.getRegrasAlteracao());
        servico.setRegrasCancelamento(dto.getRegrasCancelamento());
        servico.setObservacoes(dto.getObservacoes());
        taxasAtuais.removeIf(atual -> taxasResolvidas.stream().noneMatch(resolvida -> resolvida == atual));
        for (Taxa taxa : taxasResolvidas) {
            if (taxasAtuais.stream().noneMatch(atual -> atual == taxa)) {
                taxasAtuais.add(taxa);
            }
        }
    }

    public void resolver(List<? extends ServicoDTO> dtos, List<? extends Servico> servicos) {
        if (dtos == null || servicos == null) {
            return;
        }
        int quantidade = Math.min(dtos.size(), servicos.size());
        for (int indice = 0; indice < quantidade; indice++) {
            resolver(dtos.get(indice), servicos.get(indice));
        }
    }

    public void validarMoedaOrcamento(List<? extends ServicoDTO> dtos, Moeda moedaOrcamento) {
        if (dtos == null) {
            return;
        }
        for (ServicoDTO dto : dtos) {
            if (dto != null && dto.getMoeda() != moedaOrcamento) {
                throw new IllegalArgumentException("A moeda de todos os serviços deve ser igual à moeda do orçamento.");
            }
        }
    }

    private void validarValores(ServicoDTO dto) {
        if (dto.getMoeda() == null) {
            throw new IllegalArgumentException("Informe a moeda do serviço.");
        }
        if (dto.getValor() == null) {
            throw new IllegalArgumentException("Informe o valor do serviço.");
        }
        if (dto.getDesconto() != null && dto.getDesconto().signum() < 0) {
            throw new IllegalArgumentException("O desconto não pode ser negativo.");
        }
        if (dto.getDesconto() != null && dto.getValor() != null && dto.getDesconto().compareTo(dto.getValor()) > 0) {
            throw new IllegalArgumentException("O desconto não pode ser maior que o valor do serviço.");
        }
        if (dto.getValor() != null && dto.getValor().signum() < 0) {
            throw new IllegalArgumentException("O valor do serviço não pode ser negativo.");
        }
    }
}
