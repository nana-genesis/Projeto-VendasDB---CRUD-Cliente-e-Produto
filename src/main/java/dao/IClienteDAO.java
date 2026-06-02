package dao;

import domain.Cliente;
import java.util.List;

public interface IClienteDAO {
    Integer cadastrar(Cliente cliente) throws Exception;
    Integer atualizar(Cliente cliente) throws Exception;
    Cliente buscar(String codigo) throws Exception;
    List<Cliente> buscarTodos() throws Exception;
    Integer excluir(Cliente cliente) throws Exception;
}