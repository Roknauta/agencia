package com.roknauta.agencia.repository;

import com.roknauta.agencia.domain.BagagemCompanhiaAerea;
import com.roknauta.agencia.domain.TipoBagagem;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BagagemCompanhiaAereaRepository extends JpaRepository<BagagemCompanhiaAerea, Long> {

    @Query("""
            select bagagem
            from BagagemCompanhiaAerea bagagem
            join fetch bagagem.companhiaAerea companhia
            join fetch companhia.pais
            where (:companhiaId is null or companhia.id = :companhiaId)
              and (:tipoBagagem is null or bagagem.tipoBagagem = :tipoBagagem)
              and (:dataReferencia is null or bagagem.dataReferencia = :dataReferencia)
            order by companhia.nome, bagagem.tipoBagagem, bagagem.dataReferencia desc
            """)
    List<BagagemCompanhiaAerea> buscar(@Param("companhiaId") Long companhiaId,
                                       @Param("tipoBagagem") TipoBagagem tipoBagagem,
                                       @Param("dataReferencia") LocalDate dataReferencia);

    @Query("""
            select bagagem
            from BagagemCompanhiaAerea bagagem
            join fetch bagagem.companhiaAerea companhia
            join fetch companhia.pais
            where (:texto = '' or lower(companhia.nome) like lower(concat('%', :texto, '%')))
            order by companhia.nome, bagagem.tipoBagagem, bagagem.dataReferencia desc
            """)
    List<BagagemCompanhiaAerea> buscarPorTexto(@Param("texto") String texto);
}
