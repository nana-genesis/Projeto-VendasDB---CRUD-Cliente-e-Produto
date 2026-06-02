package dao;

import dao.generic.jdbc.ConnectionFactory;
import domain.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements IProdutoDAO {

    @Override
    public Integer cadastrar(Produto produto) throws Exception {
        String sql = "INSERT INTO public.produto (nome, codigo, preco, quantidade) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCodigo());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());
            return stmt.executeUpdate();
        }
    }

    @Override
    public Integer atualizar(Produto produto) throws Exception {
        String sql = "UPDATE public.produto SET nome = ?, preco = ?, quantidade = ? WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setBigDecimal(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setString(4, produto.getCodigo());
            return stmt.executeUpdate();
        }
    }

    @Override
    public Produto buscar(String codigo) throws Exception {
        String sql = "SELECT * FROM public.produto WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigo(rs.getString("codigo"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setQuantidade(rs.getInt("quantidade"));
                return produto;
            }
            return null;
        }
    }

    @Override
    public List<Produto> buscarTodos() throws Exception {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM public.produto ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setCodigo(rs.getString("codigo"));
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
}
