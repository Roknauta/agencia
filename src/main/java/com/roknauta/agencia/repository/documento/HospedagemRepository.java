package com.roknauta.agencia.repository.documento;

import java.util.List;

import com.roknauta.agencia.domain.documento.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {
    @Query("select e from Hospedagem e where (:texto = '' or lower(e.localizador) like lower(concat('%', :texto, '%')) or lower(e.hotelSelecionado.nome) like lower(concat('%', :texto, '%')))")
    List<Hospedagem> buscar(@Param("texto") String texto);
}
