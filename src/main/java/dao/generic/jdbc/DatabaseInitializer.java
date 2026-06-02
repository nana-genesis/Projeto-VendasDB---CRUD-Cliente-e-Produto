package dao.generic.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() throws SQLException {
        String createClienteTable = """
                CREATE TABLE IF NOT EXISTS public.cliente (
                    id BIGSERIAL PRIMARY KEY,
                    nome VARCHAR(150) NOT NULL,
                    codigo VARCHAR(50) NOT NULL UNIQUE,
                    telefone VARCHAR(20) NOT NULL,
                    email VARCHAR(150) NOT NULL
                )
                """;

        String createProdutoTable = """
                CREATE TABLE IF NOT EXISTS public.produto (
                    id BIGSERIAL PRIMARY KEY,
                    nome VARCHAR(150) NOT NULL,
                    codigo VARCHAR(50) NOT NULL UNIQUE,
                    preco NUMERIC(10,2) NOT NULL,
                    quantidade INTEGER NOT NULL
                )
                """;

        String createTriggerFunction = """
                CREATE OR REPLACE FUNCTION atualizar_estoque()
                RETURNS TRIGGER AS $$
                BEGIN
                    UPDATE public.produto
                    SET quantidade = quantidade - NEW.quantidade
                    WHERE id = NEW.produto_id;
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createClienteTable);
            stmt.execute(createProdutoTable);
            stmt.execute(createTriggerFunction);
        }
    }
}
