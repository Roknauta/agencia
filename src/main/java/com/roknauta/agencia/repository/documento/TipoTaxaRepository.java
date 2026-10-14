package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TipoTaxa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoTaxaRepository extends JpaRepository<TipoTaxa, Long> {

    @Query("""
        select tipoTaxa from TipoTaxa tipoTaxa
        where (:nome = '' or lower(tipoTaxa.nome) like lower(concat('%', :nome, '%')))
          and (:ativo is null or tipoTaxa.ativo = :ativo)
        order by tipoTaxa.nome
        """)
    List<TipoTaxa> buscar(@Param("nome") String nome, @Param("ativo") Boolean ativo);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
