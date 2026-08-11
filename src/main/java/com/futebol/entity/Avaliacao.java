package com.futebol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacoes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Usuario avaliador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avaliado_id", nullable = false)
    private Usuario avaliado;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer nota; // 1 a 5

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}
