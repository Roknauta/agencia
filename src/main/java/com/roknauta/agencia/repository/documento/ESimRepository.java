package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.ESim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ESimRepository extends JpaRepository<ESim, Long> {
    @Query("select e from ESim e where (:texto = '' or lower(e.operadora) like lower(concat('%', :texto, '%')) or lower(e.plano) like lower(concat('%', :texto, '%')))")
    List<ESim> buscar(@Param("texto") String texto);
}
