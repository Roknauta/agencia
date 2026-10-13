package com.roknauta.agencia.repository.documento;

import com.roknauta.agencia.domain.SentidoVoo;
import com.roknauta.agencia.domain.documento.TrajetoAereo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrajetoAereoRepository extends JpaRepository<TrajetoAereo, Long> {

    @Query("select e from TrajetoAereo e where (:sentido is null or e.sentido = :sentido)")
    List<TrajetoAereo> buscar(@Param("sentido") SentidoVoo sentido);
}
