package dev.gustavoesposar.model;

import java.math.BigDecimal;

public final class Produto {
    private int idProduto;
    private String categoria;
    private String marca;
    private String nome;
    private BigDecimal preco;
    private String fornecedor;
    private int qtde;
    private int qtdeMinima;
    private int qtdeMaxima;

    public Produto() {
    }

    public Produto(int idProduto, String categoria, String marca, String nome, BigDecimal precoUnitario,
            String fornecedor, int qtde, int qtdeMinima, int qtdeMaxima) {
        this.idProduto = idProduto;
        this.categoria = categoria;
        this.marca = marca;
        this.nome = nome;
        this.preco = precoUnitario;
        this.fornecedor = fornecedor;
        this.qtde = qtde;
        this.qtdeMinima = qtdeMinima;
        this.qtdeMaxima = qtdeMaxima;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal precoUnitario) {
        this.preco = precoUnitario;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public int getQtdeMinima() {
        return qtdeMinima;
    }

    public void setQtdeMinima(int qtdeMinima) {
        this.qtdeMinima = qtdeMinima;
    }

    public int getQtdeMaxima() {
        return qtdeMaxima;
    }

    public void setQtdeMaxima(int qtdeMaxima) {
        this.qtdeMaxima = qtdeMaxima;
    }
}
