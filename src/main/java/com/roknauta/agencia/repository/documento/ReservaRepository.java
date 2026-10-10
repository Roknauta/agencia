package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("select e from Reserva e where (:texto = '' or lower(e.localizador) like lower(concat('%', :texto, '%')))")
    List<Reserva> buscar(@Param("texto") String texto);
}
