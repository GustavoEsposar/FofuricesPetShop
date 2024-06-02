package dev.gustavoesposar.model;

import java.math.BigDecimal;

public final class Produto {
    private int idProduto;
    private String categoria;
    private String marca;
    private String nome;
    private BigDecimal precoUnitario;

    public Produto() {
    }

    public Produto(int idProduto, String categoria, String marca, String nome, BigDecimal precoUnitario) {
        this.idProduto = idProduto;
        this.categoria = categoria;
        this.marca = marca;
        this.nome = nome;
        this.precoUnitario = precoUnitario;
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

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
