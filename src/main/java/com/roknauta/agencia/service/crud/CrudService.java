package com.roknauta.agencia.service.crud;

import java.util.List;

import com.roknauta.agencia.dto.BaseDTO;


public interface CrudService<T extends BaseDTO> {

    T findById(Long id);

    List<T> find(T dto);

    List<T> findAll();

    T save(T dto);

    void delete(Long id);
}
