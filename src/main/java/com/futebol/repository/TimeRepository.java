package com.futebol.repository;

import com.futebol.entity.Time;
import com.futebol.enums.StatusDesafio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TimeRepository extends JpaRepository<Time, String> {
    List<Time> findByUsuarioId(String usuarioId);
    long countByUsuarioId(String usuarioId);
    Optional<Time> findByIdAndUsuarioId(String id, String usuarioId);

    // Busca paginada com filtros opcionais (status, cidade, bairro).
    // "busca" é um termo livre que casa com nome, bairro OU cidade.
    // Qualquer parâmetro nulo é ignorado na cláusula WHERE.
    @Query("""
        SELECT t FROM Time t
        WHERE (:status IS NULL OR t.statusDesafio = :status)
          AND (
                CAST(:cidade AS string) IS NULL
                OR LOWER(t.cidade) LIKE LOWER(CONCAT('%', CAST(:cidade AS string), '%'))
          )
          AND (
                CAST(:bairro AS string) IS NULL
                OR LOWER(t.bairro) LIKE LOWER(CONCAT('%', CAST(:bairro AS string), '%'))
          )
          AND (
                CAST(:busca AS string) IS NULL
                OR LOWER(t.nome) LIKE LOWER(CONCAT('%', CAST(:busca AS string), '%'))
                OR LOWER(t.bairro) LIKE LOWER(CONCAT('%', CAST(:busca AS string), '%'))
                OR LOWER(t.cidade) LIKE LOWER(CONCAT('%', CAST(:busca AS string), '%'))
          )
        """)
    Page<Time> buscar(
            @Param("status") StatusDesafio status,
            @Param("cidade") String cidade,
            @Param("bairro") String bairro,
            @Param("busca") String busca,
            Pageable pageable);
}

