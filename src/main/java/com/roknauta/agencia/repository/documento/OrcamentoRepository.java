package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    @Query("select e from Orcamento e where (:cliente = '' or lower(e.cliente) like lower(concat('%', :cliente, '%')) "
        + "or lower(e.numero) like lower(concat('%', :cliente, '%'))) "
        + "and (:origemId is null or e.origem.id = :origemId) "
        + "and (:destino = '' or lower(e.destino) like lower(concat('%', :destino, '%'))) "
        + "and (:inicio is null or e.inicio = :inicio) and (:fim is null or e.fim = :fim)")
    List<Orcamento> buscar(@Param("cliente") String cliente, @Param("origemId") Long origemId,
        @Param("destino") String destino, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("select distinct o from Orcamento o join o.segurosViagem s where s.id = :seguroId")
    java.util.Optional<Orcamento> findBySeguroViagemId(@Param("seguroId") Long seguroId);

    @Query("select distinct o from Orcamento o join o.eSims e where e.id = :eSimId")
    java.util.Optional<Orcamento> findByESimId(@Param("eSimId") Long eSimId);

}
