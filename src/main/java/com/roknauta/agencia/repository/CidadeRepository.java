package com.roknauta.agencia.repository;

import com.roknauta.agencia.domain.Cidade;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CidadeRepository extends JpaRepository<Cidade, Long> {

    @Query("""
        select cidade
        from Cidade cidade
        left join fetch cidade.pais
        where (:nome = '' or lower(cidade.nome) like lower(concat('%', :nome, '%')))
          and (:estado = '' or lower(cidade.estado) like lower(concat('%', :estado, '%')))
          and (:paisId is null or cidade.pais.id = :paisId)
        order by cidade.nome
        """)
    List<Cidade> buscar(@Param("nome") String nome, @Param("estado") String estado, @Param("paisId") Long paisId);

    @Query("""
        select cidade
        from Cidade cidade
        left join fetch cidade.pais
        where (:texto = '' or lower(cidade.nome) like lower(concat('%', :texto, '%')))
        order by cidade.nome
        """)
    List<Cidade> buscarPorTexto(@Param("texto") String texto);
}
