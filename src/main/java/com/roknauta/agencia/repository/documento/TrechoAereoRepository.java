package com.roknauta.agencia.repository.documento;

import java.time.LocalDate;
import java.util.List;

import com.roknauta.agencia.domain.documento.TrechoAereo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrechoAereoRepository extends JpaRepository<TrechoAereo, Long> {

    @Query("""
        select distinct trecho
        from TrechoAereo trecho
        join fetch trecho.voo voo
        join fetch voo.companhiaAerea
        join fetch voo.origem
        join fetch voo.destino
        join fetch trecho.tarifaAerea tarifa
        join fetch tarifa.companhiaAerea
        where (:numeroVoo = '' or lower(voo.numero) like lower(concat('%', :numeroVoo, '%')))
          and (:dataPartida is null or trecho.dataPartida = :dataPartida)
          and (:tarifaId is null or tarifa.id = :tarifaId)
        order by trecho.dataPartida, voo.partida
        """)
    List<TrechoAereo> buscar(@Param("numeroVoo") String numeroVoo, @Param("dataPartida") LocalDate dataPartida,
        @Param("tarifaId") Long tarifaId);
}
