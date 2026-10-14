package com.roknauta.agencia.repository.documento;

import com.roknauta.agencia.domain.documento.Taxa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxaRepository extends JpaRepository<Taxa, Long> {

    boolean existsByTipoTaxaId(Long idTipoTaxa);
}
