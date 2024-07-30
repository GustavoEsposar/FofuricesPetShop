package dev.gustavoesposar.model;

import java.math.BigDecimal;

public final class Pet {

    private int idPet;
    private String especieNome;
    private String racaNome;
    private String fornecedorNome;
    private BigDecimal valor;
    private String idade;
    private int qtde;

    public Pet() {
    }

    public Pet(int idPet, String especieNome, String racaNome, String fornecedorNome, BigDecimal valor, String idade, int qtde) {
        this.idPet = idPet;
        this.especieNome = especieNome;
        this.racaNome = racaNome;
        this.fornecedorNome = fornecedorNome;
        this.valor = valor;
        this.idade = idade;
        this.qtde = qtde;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getEspecieNome() {
        return especieNome;
    }

    public void setEspecieNome(String especieNome) {
        this.especieNome = especieNome;
    }

    public String getRacaNome() {
        return racaNome;
    }

    public void setRacaNome(String racaNome) {
        this.racaNome = racaNome;
    }

    public String getFornecedorNome() {
        return fornecedorNome;
    }

    public void setFornecedorNome(String fornecedorNome) {
        this.fornecedorNome = fornecedorNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }
}
