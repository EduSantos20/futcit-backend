package com.futebol.dto;

import lombok.*;

import java.time.LocalDateTime;

public class FotoDTO {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private String id;
        private String autorId;
        private String autorNome;
        private String autorFoto;
        private String timeId;
        private String timeNome;
        private String comentario;
        private String imagemUrl;
        private LocalDateTime criadoEm;
    }
}
