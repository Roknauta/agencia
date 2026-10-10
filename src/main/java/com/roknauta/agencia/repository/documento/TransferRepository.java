package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("select e from Transfer e where (:texto = '' or lower(e.prestador) like lower(concat('%', :texto, '%')))")
    List<Transfer> buscar(@Param("texto") String texto);
}
