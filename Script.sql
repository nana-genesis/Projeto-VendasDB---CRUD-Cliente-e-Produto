CREATE TABLE IF NOT EXISTS public.cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.produto (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL
);

CREATE OR REPLACE FUNCTION atualizar_estoque()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE public.produto
    SET quantidade = quantidade - NEW.quantidade
    WHERE id = NEW.produto_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
