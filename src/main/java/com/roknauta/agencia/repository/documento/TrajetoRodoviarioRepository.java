package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.TrajetoRodoviario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrajetoRodoviarioRepository extends JpaRepository<TrajetoRodoviario, Long> {

    @Query("select e from TrajetoRodoviario e where (:texto = '' or lower(e.transportadora) like lower(concat('%', :texto, '%')))")
    List<TrajetoRodoviario> buscar(@Param("texto") String texto);
}
