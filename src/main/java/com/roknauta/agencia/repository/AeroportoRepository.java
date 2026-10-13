package com.roknauta.agencia.repository;

import java.util.List;

import com.roknauta.agencia.domain.Aeroporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AeroportoRepository extends JpaRepository<Aeroporto, Long> {

    @Query("""
        select aeroporto
        from Aeroporto aeroporto
        join fetch aeroporto.cidadeAtendida cidade
        left join fetch cidade.pais
        where (:nome = '' or lower(aeroporto.nome) like lower(concat('%', :nome, '%')))
          and (:cidadeId is null or aeroporto.cidadeAtendida.id = :cidadeId)
          and (:codigoIata = '' or lower(aeroporto.codigoIata) like lower(concat('%', :codigoIata, '%')))
        order by aeroporto.nome
        """)
    List<Aeroporto> buscar(@Param("nome") String nome, @Param("cidadeId") Long cidadeId,
        @Param("codigoIata") String codigoIata);
}
