
CREATE TABLE movimentacoes (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_movimentacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_movimentacao_produto
        FOREIGN KEY (produto_id)
        REFERENCES produtos(id),

    CONSTRAINT chk_movimentacao_tipo
        CHECK (tipo IN ('ENTRADA', 'SAIDA')),

    CONSTRAINT chk_movimentacao_quantidade
        CHECK (quantidade > 0)
);
