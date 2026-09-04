package com.futebol.repository;

import com.futebol.entity.FotoJogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoJogoRepository extends JpaRepository<FotoJogo, String> {

    // Fotos postadas por um usuário (aparecem no perfil do jogador/dono)
    List<FotoJogo> findByAutorIdOrderByCriadoEmDesc(String autorId);

    // Fotos relacionadas a um time (aparecem no perfil do time)
    List<FotoJogo> findByTimeIdOrderByCriadoEmDesc(String timeId);
}
