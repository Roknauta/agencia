package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    @Query("select e from Orcamento e where (:numero = '' or lower(e.numero) like lower(concat('%', :numero, '%'))) and (:cliente = '' or lower(e.cliente) like lower(concat('%', :cliente, '%')))")
    List<Orcamento> buscar(@Param("numero") String numero, @Param("cliente") String cliente);
}
