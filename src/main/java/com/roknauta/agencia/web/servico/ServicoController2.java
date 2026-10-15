package com.roknauta.agencia.web.servico;

import com.roknauta.agencia.dto.documento.OrcamentoDTO;
import com.roknauta.agencia.dto.documento.ServicoDTO;
import com.roknauta.agencia.dto.documento.TaxaDTO;
import com.roknauta.agencia.web.ControllerBase;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.core.GenericTypeResolver;

public abstract class ServicoController2<T extends ServicoDTO> extends ControllerBase {

    @Getter
    @Setter
    protected T editForm;
    @Getter
    @Setter
    protected OrcamentoDTO orcamento;
    @Getter
    @Setter
    protected TaxaDTO taxaForm;
    @Getter
    protected boolean editing;

    @PostConstruct
    public void init() {}

    @SneakyThrows
    public void startNew() {
        editForm = getEditFormClass().getDeclaredConstructor().newInstance();
        editing = true;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEditFormClass(){
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), ServicoController2.class);
        return (Class<T>) typeArguments[0];
    }

}
