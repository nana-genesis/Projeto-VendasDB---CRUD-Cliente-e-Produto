import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.generic.jdbc.DatabaseInitializer;
import domain.Cliente;
import domain.Produto;
import java.math.BigDecimal;

class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTE DE CONEXÃO ===");
            DatabaseInitializer.initialize();

            // Teste Cliente
            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("João Teste", "001", "11999999999", "joao@teste.com");
            clienteDAO.cadastrar(cliente);
            System.out.println("Cliente cadastrado com sucesso!");

            // Teste Produto
            ProdutoDAO produtoDAO = new ProdutoDAO();
            Produto produto = new Produto("Notebook", "P001", new BigDecimal("2500.00"), 10);
            produtoDAO.cadastrar(produto);
            System.out.println("Produto cadastrado com sucesso!");

            // Listar
            System.out.println("\nClientes cadastrados:");
            clienteDAO.buscarTodos().forEach(System.out::println);

            System.out.println("\nProdutos cadastrados:");
            produtoDAO.buscarTodos().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
