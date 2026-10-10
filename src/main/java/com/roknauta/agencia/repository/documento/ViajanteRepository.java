package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Viajante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ViajanteRepository extends JpaRepository<Viajante, Long> {
    @Query("select e from Viajante e where (:texto = '' or lower(e.nome) like lower(concat('%', :texto, '%')) or lower(e.documento) like lower(concat('%', :texto, '%')))")
    List<Viajante> buscar(@Param("texto") String texto);
}
