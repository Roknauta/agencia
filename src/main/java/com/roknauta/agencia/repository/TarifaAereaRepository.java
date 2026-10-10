package com.roknauta.agencia.repository;

import java.time.LocalDate;
import java.util.List;

import com.roknauta.agencia.domain.TarifaAerea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TarifaAereaRepository extends JpaRepository<TarifaAerea, Long> {

    @Query("""
            select distinct tarifa
            from TarifaAerea tarifa
            join fetch tarifa.companhiaAerea companhia
            join fetch companhia.pais
            left join fetch tarifa.bagagensInclusas bagagem
            left join fetch bagagem.companhiaAerea bagagemCompanhia
            left join fetch bagagemCompanhia.pais
            where (:nome = '' or lower(tarifa.nome) like lower(concat('%', :nome, '%')))
              and (:companhiaId is null or companhia.id = :companhiaId)
              and (:dataReferencia is null or tarifa.dataReferencia = :dataReferencia)
            order by companhia.nome, tarifa.nome, tarifa.dataReferencia desc
            """)
    List<TarifaAerea> buscar(@Param("nome") String nome, @Param("companhiaId") Long companhiaId,
                             @Param("dataReferencia") LocalDate dataReferencia);
}
