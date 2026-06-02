package dao;

import dao.generic.jdbc.ConnectionFactory;
import domain.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements IProdutoDAO {

    @Override
    public Integer cadastrar(Produto produto) throws Exception {
        String insertProdutoSql = "INSERT INTO public.produto (nome, codigo, descricao, preco, quantidade) VALUES (?, ?, ?, ?, ?)";
        String upsertEstoqueSql = "INSERT INTO public.estoque (produto_id, quantidade) VALUES (?, ?) " +
                "ON CONFLICT (produto_id) DO UPDATE SET quantidade = EXCLUDED.quantidade";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int rows;
                long produtoId;

                try (PreparedStatement stmt = conn.prepareStatement(insertProdutoSql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, produto.getNome());
                    stmt.setString(2, produto.getCodigo());
                    stmt.setString(3, produto.getDescricao());
                    stmt.setBigDecimal(4, produto.getPreco());
                    stmt.setInt(5, quantidadeOuZero(produto.getQuantidade()));
                    rows = stmt.executeUpdate();

                    if (rows == 0) {
                        throw new SQLException("Falha ao cadastrar produto");
                    }

                    try (ResultSet keys = stmt.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Nao foi possivel obter o ID do produto");
                        }
                        produtoId = keys.getLong(1);
                    }
                }

                try (PreparedStatement estoqueStmt = conn.prepareStatement(upsertEstoqueSql)) {
                    estoqueStmt.setLong(1, produtoId);
                    estoqueStmt.setInt(2, quantidadeOuZero(produto.getQuantidade()));
                    estoqueStmt.executeUpdate();
                }

                conn.commit();
                return rows;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        }
    }

    @Override
    public Integer atualizar(Produto produto) throws Exception {
        String selectIdSql = "SELECT id FROM public.produto WHERE codigo = ?";
        String updateSql = "UPDATE public.produto SET nome = ?, descricao = ?, preco = ?, quantidade = ? WHERE id = ?";
        String upsertEstoqueSql = "INSERT INTO public.estoque (produto_id, quantidade) VALUES (?, ?) " +
                "ON CONFLICT (produto_id) DO UPDATE SET quantidade = EXCLUDED.quantidade";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long produtoId = buscarProdutoId(conn, selectIdSql, produto.getCodigo());
                if (produtoId == null) {
                    conn.rollback();
                    return 0;
                }

                int rows;
                try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
                    stmt.setString(1, produto.getNome());
                    stmt.setString(2, produto.getDescricao());
                    stmt.setBigDecimal(3, produto.getPreco());
                    stmt.setInt(4, quantidadeOuZero(produto.getQuantidade()));
                    stmt.setLong(5, produtoId);
                    rows = stmt.executeUpdate();
                }

                try (PreparedStatement estoqueStmt = conn.prepareStatement(upsertEstoqueSql)) {
                    estoqueStmt.setLong(1, produtoId);
                    estoqueStmt.setInt(2, quantidadeOuZero(produto.getQuantidade()));
                    estoqueStmt.executeUpdate();
                }

                conn.commit();
                return rows;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        }
    }

    @Override
    public Integer baixarEstoque(String codigo, Integer quantidadeVendida) throws Exception {
        if (quantidadeVendida == null || quantidadeVendida <= 0) {
            throw new IllegalArgumentException("Quantidade vendida deve ser maior que zero");
        }

        String selectSql = """
                SELECT p.id, COALESCE(e.quantidade, p.quantidade, 0) AS quantidade
                FROM public.produto p
                LEFT JOIN public.estoque e ON e.produto_id = p.id
                WHERE p.codigo = ?
                """;
        String upsertEstoqueSql = "INSERT INTO public.estoque (produto_id, quantidade) VALUES (?, ?) " +
                "ON CONFLICT (produto_id) DO UPDATE SET quantidade = EXCLUDED.quantidade";
        String updateProdutoSql = "UPDATE public.produto SET quantidade = quantidade - ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Long produtoId = null;
                int quantidadeAtual = 0;

                try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
                    stmt.setString(1, codigo);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            produtoId = rs.getLong("id");
                            quantidadeAtual = rs.getInt("quantidade");
                        }
                    }
                }

                if (produtoId == null) {
                    conn.rollback();
                    throw new SQLException("Produto nao encontrado para baixa de estoque");
                }

                if (quantidadeAtual < quantidadeVendida) {
                    conn.rollback();
                    throw new SQLException("Estoque insuficiente para a venda");
                }

                int novaQuantidade = quantidadeAtual - quantidadeVendida;

                try (PreparedStatement estoqueStmt = conn.prepareStatement(upsertEstoqueSql)) {
                    estoqueStmt.setLong(1, produtoId);
                    estoqueStmt.setInt(2, novaQuantidade);
                    estoqueStmt.executeUpdate();
                }

                try (PreparedStatement produtoStmt = conn.prepareStatement(updateProdutoSql)) {
                    produtoStmt.setInt(1, quantidadeVendida);
                    produtoStmt.setLong(2, produtoId);
                    produtoStmt.executeUpdate();
                }

                conn.commit();
                return 1;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw e;
            }
        }
    }

    @Override
    public Produto buscar(String codigo) throws Exception {
        String sql = """
                SELECT p.id, p.nome, p.codigo, p.descricao, p.preco,
                       COALESCE(e.quantidade, p.quantidade) AS quantidade
                FROM public.produto p
                LEFT JOIN public.estoque e ON e.produto_id = p.id
                WHERE p.codigo = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getLong("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setCodigo(rs.getString("codigo"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    return produto;
                }
            }
            return null;
        }
    }

    @Override
    public List<Produto> buscarTodos() throws Exception {
        List<Produto> lista = new ArrayList<>();
        String sql = """
                SELECT p.id, p.nome, p.codigo, p.descricao, p.preco,
                       COALESCE(e.quantidade, p.quantidade) AS quantidade
                FROM public.produto p
                LEFT JOIN public.estoque e ON e.produto_id = p.id
                ORDER BY p.id
                """;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setQuantidade(rs.getInt("quantidade"));
                lista.add(produto);
            }
        }
        return lista;
    }

    @Override
    public Integer excluir(Produto produto) throws Exception {
        String sql = "DELETE FROM public.produto WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getCodigo());
            return stmt.executeUpdate();
        }
    }

    private Long buscarProdutoId(Connection conn, String sql, String codigo) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id");
                }
            }
        }
        return null;
    }

    private int quantidadeOuZero(Integer quantidade) {
        return quantidade == null ? 0 : quantidade;
    }
}
