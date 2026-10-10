package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.FotoHospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotoHospedagemRepository extends JpaRepository<FotoHospedagem, Long> {
    @Query("select e from FotoHospedagem e where (:hospedagemId is null or e.hospedagem.id = :hospedagemId)")
    List<FotoHospedagem> buscar(@Param("hospedagemId") Long hospedagemId);
}
