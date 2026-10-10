package com.roknauta.agencia.service.crud;

import java.util.List;

import com.roknauta.agencia.dto.BaseDTO;
import com.roknauta.agencia.web.Msg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public abstract class CrudServiceBase<E, D extends BaseDTO>
        implements CrudService<D> {

    protected abstract JpaRepository<E, Long> getRepository();

    protected abstract D toDTO(E entity);

    protected abstract E toEntity(D dto);

    protected abstract void updateEntity(D dto, E entity);

    @Override
    @Transactional(readOnly = true)
    public D findById(Long id) {
        return getRepository()
                .findById(id)
                .map(this::toDTO)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                Msg.get("crud.registro.nao.encontrado")
                        )
                );
    }

    @Override
    public abstract List<D> find(D dto);

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll() {
        return getRepository()
                .findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public D save(D dto) {

        E entity;

        if (dto.getId() == null) {

            // CREATE
            entity = toEntity(dto);

        } else {

            // UPDATE
            entity = getRepository()
                    .findById(dto.getId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    Msg.get("crud.registro.nao.encontrado")
                            )
                    );

            updateEntity(dto, entity);
        }

        entity = getRepository().save(entity);

        return toDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!getRepository().existsById(id)) {
            throw new IllegalArgumentException(
                    Msg.get("crud.registro.nao.encontrado")
            );
        }

        getRepository().deleteById(id);
    }
}
