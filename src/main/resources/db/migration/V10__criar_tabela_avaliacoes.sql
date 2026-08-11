CREATE TABLE avaliacoes (
    id VARCHAR(255) PRIMARY KEY,
    avaliador_id VARCHAR(255) NOT NULL,
    avaliado_id VARCHAR(255) NOT NULL,
    estrelas INTEGER NOT NULL CHECK (estrelas >= 1 AND estrelas <= 5),
    comentario TEXT,
    criado_em TIMESTAMP(6) NOT NULL,
    CONSTRAINT fk_avaliador FOREIGN KEY (avaliador_id) REFERENCES usuarios(id),
    CONSTRAINT fk_avaliado FOREIGN KEY (avaliado_id) REFERENCES usuarios(id)
);
