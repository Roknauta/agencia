package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.BagagemTrechoAereo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BagagemTrechoAereoRepository extends JpaRepository<BagagemTrechoAereo, Long> {

    @Query("select e from BagagemTrechoAereo e where (:texto = '' or lower(e.descricao) like lower(concat('%', :texto, '%'))) "
        + "and (:bagagemId is null or e.bagagemCompanhiaAerea.id = :bagagemId)")
    List<BagagemTrechoAereo> buscar(@Param("texto") String texto, @Param("bagagemId") Long bagagemId);
}
