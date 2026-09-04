-- ============================================================
-- V11__criar_tabela_fotos_jogo.sql
-- Fotos postadas por jogadores/donos de time nos perfis
-- ============================================================
CREATE TABLE IF NOT EXISTS fotos_jogo (
    id           VARCHAR(36) PRIMARY KEY,
    autor_id     VARCHAR(36) NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
    time_id      VARCHAR(36) REFERENCES times(id) ON DELETE CASCADE,
    imagem       BYTEA NOT NULL,
    comentario   TEXT,
    criado_em    TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_fotos_jogo_autor ON fotos_jogo(autor_id);
CREATE INDEX IF NOT EXISTS idx_fotos_jogo_time  ON fotos_jogo(time_id);
