package com.futebol.dto;

import lombok.Data;
import java.time.LocalDateTime;

public class AvaliacaoDTO {
    @Data
    public static class Request {
        public String avaliadoId;
        public Integer nota;
        public String comentarios;
    }

    @Data
    public static class Response {
        public String id;
        public String avaliadorNome;
        public Integer nota;
        public String comentarios;
        public LocalDateTime criadoEm;
    }
}
