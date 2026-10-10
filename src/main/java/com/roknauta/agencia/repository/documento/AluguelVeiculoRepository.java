package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.AluguelVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AluguelVeiculoRepository extends JpaRepository<AluguelVeiculo, Long> {
    @Query("select e from AluguelVeiculo e where (:texto = '' or lower(e.localizador) like lower(concat('%', :texto, '%')))")
    List<AluguelVeiculo> buscar(@Param("texto") String texto);
}
