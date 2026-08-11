package com.futebol.repository;

import com.futebol.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, String> {
    List<Avaliacao> findByAvaliadoIdOrderByCriadoEmDesc(String avaliadoId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.avaliado.id = :avaliadoId")
    Double mediaEstrelas(@Param("avaliadoId") String avaliadoId);
}
