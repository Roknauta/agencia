package com.roknauta.agencia.service.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.DocumentoViagem;
import com.roknauta.agencia.domain.documento.TrajetoAereo;
import com.roknauta.agencia.domain.documento.TrechoAereo;
import com.roknauta.agencia.domain.documento.Voo;
import com.roknauta.agencia.dto.documento.DocumentoViagemDTO;
import com.roknauta.agencia.dto.documento.TrajetoAereoDTO;
import com.roknauta.agencia.dto.documento.TrechoAereoDTO;
import com.roknauta.agencia.repository.documento.TrechoAereoRepository;
import com.roknauta.agencia.repository.documento.VooRepository;
import com.roknauta.agencia.service.mapper.TrechoAereoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrechoAereoService {

    private final TrechoAereoRepository repository;
    private final VooRepository vooRepository;
    private final TrechoAereoMapper mapper;

    public TrechoAereoService(TrechoAereoRepository repository, VooRepository vooRepository, TrechoAereoMapper mapper) {
        this.repository = repository;
        this.vooRepository = vooRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public TrechoAereoDTO buscarPorId(Long id) {
        return repository.findById(id).map(mapper::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Trecho aéreo não encontrado."));
    }

    @Transactional(readOnly = true)
    public List<TrechoAereoDTO> buscar(TrechoAereoDTO filtro) {
        String numeroVoo = filtro.getVoo() == null ? ""
            : StringUtils.defaultString(StringUtils.trimToNull(filtro.getVoo().getNumero()));
        return repository.buscar(numeroVoo, filtro.getDataPartida()).stream().map(mapper::toDTO).toList();
    }

    @Transactional
    public TrechoAereoDTO salvar(TrechoAereoDTO dto) {
        TrechoAereo trecho;
        if (dto.getId() == null) {
            trecho = mapper.toEntity(dto);
        } else {
            trecho = repository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Trecho aéreo não encontrado."));
            mapper.updateEntity(dto, trecho);
        }
        return mapper.toDTO(repository.save(trecho));
    }

    public void resolverReferencias(TrajetoAereoDTO dto, TrajetoAereo trajeto) {
        if (dto.getTrechos() == null || dto.getTrechos().isEmpty()) {
            return;
        }
        if (trajeto.getTrechos() == null || trajeto.getTrechos().size() != dto.getTrechos().size()) {
            throw new IllegalArgumentException("A lista de trechos do trajeto aéreo está inconsistente.");
        }
        for (int i = 0; i < dto.getTrechos().size(); i++) {
            setReferences(dto.getTrechos().get(i), trajeto.getTrechos().get(i));
        }
    }

    public void resolverReferencias(DocumentoViagemDTO dto, DocumentoViagem documento) {
        if (dto.getTrajetosAereos() == null || dto.getTrajetosAereos().isEmpty()) {
            return;
        }
        if (documento.getTrajetosAereos() == null
            || documento.getTrajetosAereos().size() != dto.getTrajetosAereos().size()) {
            throw new IllegalArgumentException("A lista de trajetos aéreos está inconsistente.");
        }
        for (int i = 0; i < dto.getTrajetosAereos().size(); i++) {
            resolverReferencias(dto.getTrajetosAereos().get(i), documento.getTrajetosAereos().get(i));
        }
    }

    private void setReferences(TrechoAereoDTO dto, TrechoAereo trecho) {
        if (dto.getVoo() == null || dto.getVoo().getId() == null || dto.getDataPartida() == null
            || dto.getDataChegada() == null || StringUtils.isBlank(dto.getTarifaAerea())) {
            throw new IllegalArgumentException("Voo, datas e descrição da tarifa são obrigatórios no trecho aéreo.");
        }
        Voo voo = vooRepository.findById(dto.getVoo().getId())
            .orElseThrow(() -> new IllegalArgumentException("Voo não encontrado."));
        trecho.setVoo(voo);
    }
}
