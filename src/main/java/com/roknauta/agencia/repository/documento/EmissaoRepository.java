package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Emissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmissaoRepository extends JpaRepository<Emissao, Long> {

    @Query("select e from Emissao e where (:numero = '' or lower(e.numero) like lower(concat('%', :numero, '%'))) and (:cliente = '' or lower(e.cliente) like lower(concat('%', :cliente, '%')))")
    List<Emissao> buscar(@Param("numero") String numero, @Param("cliente") String cliente);
}
