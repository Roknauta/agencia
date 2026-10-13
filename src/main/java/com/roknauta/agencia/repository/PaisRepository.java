package com.roknauta.agencia.repository;

import com.roknauta.agencia.domain.Pais;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PaisRepository extends JpaRepository<Pais, Long> {

    List<Pais> findByNomeContainingIgnoreCaseAndSiglaContainingIgnoreCaseOrderByNomeAsc(String nome, String sigla);

    @Query("""
        select pais
        from Pais pais
        where :nome = ''
           or lower(pais.nome) like lower(concat('%', :nome, '%'))
        order by pais.nome
        """)
    List<Pais> findByNome(@Param("nome") String nome);
}
