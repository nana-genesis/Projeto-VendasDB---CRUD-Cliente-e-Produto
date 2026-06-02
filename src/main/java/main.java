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

            ClienteDAO clienteDAO = new ClienteDAO();
            Cliente cliente = new Cliente("João Teste", "001", "11999999999", "joao@teste.com", "123.456.789-00");
            salvarOuAtualizarCliente(clienteDAO, cliente);
            System.out.println("Cliente pronto: " + clienteDAO.buscar(cliente.getCodigo()));

            ProdutoDAO produtoDAO = new ProdutoDAO();
            Produto produto = new Produto("Notebook", "P001", new BigDecimal("2500.00"), 10, "Notebook gamer");
            salvarOuAtualizarProduto(produtoDAO, produto);
            System.out.println("Produto antes da venda: " + produtoDAO.buscar(produto.getCodigo()));

            produtoDAO.baixarEstoque("P001", 3);
            System.out.println("Venda simulada: 3 unidades baixadas do estoque.");
            System.out.println("Produto depois da venda: " + produtoDAO.buscar(produto.getCodigo()));

            System.out.println("\nClientes cadastrados:");
            clienteDAO.buscarTodos().forEach(System.out::println);

            System.out.println("\nProdutos cadastrados:");
            produtoDAO.buscarTodos().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("ERRO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void salvarOuAtualizarCliente(ClienteDAO clienteDAO, Cliente cliente) throws Exception {
        if (clienteDAO.buscar(cliente.getCodigo()) == null) {
            clienteDAO.cadastrar(cliente);
        } else {
            clienteDAO.atualizar(cliente);
        }
    }

    private static void salvarOuAtualizarProduto(ProdutoDAO produtoDAO, Produto produto) throws Exception {
        if (produtoDAO.buscar(produto.getCodigo()) == null) {
            produtoDAO.cadastrar(produto);
        } else {
            produtoDAO.atualizar(produto);
        }
    }
}
