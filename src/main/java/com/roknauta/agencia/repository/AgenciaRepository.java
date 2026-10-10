package com.roknauta.agencia.repository;

import java.util.Optional;

import com.roknauta.agencia.domain.documento.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenciaRepository extends JpaRepository<Agencia, Long> {
    Optional<Agencia> findFirstByOrderByIdAsc();
}
