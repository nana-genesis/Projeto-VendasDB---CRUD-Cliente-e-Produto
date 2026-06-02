package dao;

import dao.generic.jdbc.ConnectionFactory;
import domain.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    @Override
    public Integer cadastrar(Cliente cliente) throws Exception {
        String sql = "INSERT INTO public.cliente (nome, codigo, telefone, email, cpf) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCodigo());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getCpf());
            return stmt.executeUpdate();
        }
    }

    @Override
    public Integer atualizar(Cliente cliente) throws Exception {
        String sql = "UPDATE public.cliente SET nome = ?, telefone = ?, email = ?, cpf = ? WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getCpf());
            stmt.setString(5, cliente.getCodigo());
            return stmt.executeUpdate();
        }
    }

    @Override
    public Cliente buscar(String codigo) throws Exception {
        String sql = "SELECT id, nome, codigo, telefone, email, cpf FROM public.cliente WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCodigo(rs.getString("codigo"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setCpf(rs.getString("cpf"));
                return cliente;
            }
            return null;
        }
    }

    @Override
    public List<Cliente> buscarTodos() throws Exception {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nome, codigo, telefone, email, cpf FROM public.cliente ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCodigo(rs.getString("codigo"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setCpf(rs.getString("cpf"));
                lista.add(cliente);
            }
        }
        return lista;
    }

    @Override
    public Integer excluir(Cliente cliente) throws Exception {
        String sql = "DELETE FROM public.cliente WHERE codigo = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getCodigo());
            return stmt.executeUpdate();
        }
    }
}
