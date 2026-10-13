package com.roknauta.agencia.repository;

import java.util.List;

import com.roknauta.agencia.domain.CompanhiaAerea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CompanhiaAereaRepository extends JpaRepository<CompanhiaAerea, Long> {

    @Query("""
        select companhia
        from CompanhiaAerea companhia
        join fetch companhia.pais
        where (:nome = '' or lower(companhia.nome) like lower(concat('%', :nome, '%')))
          and (:paisId is null or companhia.pais.id = :paisId)
        order by companhia.nome
        """)
    List<CompanhiaAerea> buscar(@Param("nome") String nome, @Param("paisId") Long paisId);
}
