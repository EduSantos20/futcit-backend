package com.futebol.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "fotos_jogo")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FotoJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Quem postou a foto (jogador ou dono de time)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    // Time relacionado à foto (opcional). Quando preenchido, a foto
    // aparece na galeria do perfil do time além do perfil do autor.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_id")
    private Time time;

    @JdbcTypeCode(SqlTypes.LONGVARBINARY)
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] imagem;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
