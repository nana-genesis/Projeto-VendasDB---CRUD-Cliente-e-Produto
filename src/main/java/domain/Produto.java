package domain;

import java.math.BigDecimal;

public class Produto {
    private Long id;
    private String nome;
    private String codigo;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;

    public Produto() {}

    public Produto(String nome, String codigo, BigDecimal preco, Integer quantidade) {
        this.nome = nome;
        this.codigo = codigo;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public Produto(String nome, String codigo, BigDecimal preco, Integer quantidade, String descricao) {
        this(nome, codigo, preco, quantidade);
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigo='" + codigo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                '}';
    }
}
