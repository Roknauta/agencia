package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Contrato;
import com.roknauta.agencia.domain.documento.StatusContrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    boolean existsByOrcamentoId(Long orcamentoId);

    @Query("select e from Contrato e where (:numero = '' or lower(e.numero) like lower(concat('%', :numero, '%'))) and (:cliente = '' or lower(e.cliente) like lower(concat('%', :cliente, '%'))) and (:orcamentoId is null or e.orcamento.id = :orcamentoId)")
    List<Contrato> buscar(@Param("numero") String numero, @Param("cliente") String cliente,
        @Param("orcamentoId") Long orcamentoId);

    @Query("select c from Contrato c where c.status = :status and (:texto = '' or lower(c.numero) like lower(concat('%', :texto, '%')) or lower(c.cliente) like lower(concat('%', :texto, '%'))) and not exists (select e.id from Emissao e where e.contrato.id = c.id)")
    List<Contrato> buscarDisponiveisParaEmissao(@Param("texto") String texto, @Param("status") StatusContrato status);
}
