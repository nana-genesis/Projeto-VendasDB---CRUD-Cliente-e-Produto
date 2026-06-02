CREATE TABLE IF NOT EXISTS public.cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    cpf VARCHAR(14)
);

ALTER TABLE IF EXISTS public.cliente
ADD COLUMN IF NOT EXISTS cpf VARCHAR(14);

CREATE TABLE IF NOT EXISTS public.produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL
);

ALTER TABLE IF EXISTS public.produto
ADD COLUMN IF NOT EXISTS descricao VARCHAR(255);

ALTER TABLE IF EXISTS public.produto
ADD COLUMN IF NOT EXISTS quantidade INTEGER NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS public.estoque (
    produto_id BIGINT PRIMARY KEY,
    quantidade INTEGER NOT NULL,
    CONSTRAINT estoque_produto_fk
        FOREIGN KEY (produto_id)
        REFERENCES public.produto(id)
        ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.estoque
ADD COLUMN IF NOT EXISTS quantidade INTEGER NOT NULL DEFAULT 0;

CREATE OR REPLACE FUNCTION atualizar_estoque()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE public.estoque
    SET quantidade = quantidade - NEW.quantidade
    WHERE produto_id = NEW.produto_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
