package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    boolean existsByOrcamentoId(Long orcamentoId);

    @Query("select e from Contrato e where (:numero = '' or lower(e.numero) like lower(concat('%', :numero, '%'))) and (:cliente = '' or lower(e.cliente) like lower(concat('%', :cliente, '%'))) and (:orcamentoId is null or e.orcamento.id = :orcamentoId)")
    List<Contrato> buscar(@Param("numero") String numero, @Param("cliente") String cliente,
                          @Param("orcamentoId") Long orcamentoId);
}
