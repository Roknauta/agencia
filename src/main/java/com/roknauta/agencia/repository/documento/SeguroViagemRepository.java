package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.SeguroViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeguroViagemRepository extends JpaRepository<SeguroViagem, Long> {
    @Query("select e from SeguroViagem e where (:texto = '' or lower(e.seguradora) like lower(concat('%', :texto, '%')))")
    List<SeguroViagem> buscar(@Param("texto") String texto);
}
