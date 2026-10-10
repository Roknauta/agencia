package com.roknauta.agencia.repository;

import java.util.List;

import com.roknauta.agencia.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
            select hotel
            from Hotel hotel
            join fetch hotel.cidade cidade
            left join fetch cidade.pais
            where (:nome = '' or lower(hotel.nome) like lower(concat('%', :nome, '%')))
              and (:cidadeId is null or hotel.cidade.id = :cidadeId)
            order by hotel.nome
            """)
    List<Hotel> buscar(@Param("nome") String nome, @Param("cidadeId") Long cidadeId);
}
