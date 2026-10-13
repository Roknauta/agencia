package com.roknauta.agencia.repository.documento;

import java.time.LocalDate;
import java.util.List;

import com.roknauta.agencia.domain.documento.Voo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface VooRepository extends JpaRepository<Voo, Long> {

    @Query("""
        select distinct voo
        from Voo voo
        join fetch voo.companhiaAerea companhia
        left join fetch companhia.pais
        join fetch voo.origem origem
        join fetch origem.cidadeAtendida origemCidade
        left join fetch origemCidade.pais
        join fetch voo.destino destino
        join fetch destino.cidadeAtendida destinoCidade
        left join fetch destinoCidade.pais
        where (:numero = '' or lower(voo.numero) like lower(concat('%', :numero, '%')))
          and (:companhiaId is null or companhia.id = :companhiaId)
          and (:origemId is null or origem.id = :origemId)
          and (:destinoId is null or destino.id = :destinoId)
          and (:dataReferencia is null or voo.dataReferencia = :dataReferencia)
        order by voo.dataReferencia desc, voo.numero
        """)
    List<Voo> buscar(@Param("numero") String numero, @Param("companhiaId") Long companhiaId,
        @Param("origemId") Long origemId, @Param("destinoId") Long destinoId,
        @Param("dataReferencia") LocalDate dataReferencia);
}
