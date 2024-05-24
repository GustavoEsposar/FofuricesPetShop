package dev.gustavoesposar.model;

public final class Pet {

    private int idPet;
    private int racaIdRaca;
    private Integer fornecedorIdFornecedor;
    private double valor;

    public Pet() {
    }

    public Pet(int idPet, int racaIdRaca, Integer fornecedorIdFornecedor, double valor) {
        this.idPet = idPet;
        this.racaIdRaca = racaIdRaca;
        this.fornecedorIdFornecedor = fornecedorIdFornecedor;
        this.valor = valor;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getRacaIdRaca() {
        return racaIdRaca;
    }

    public void setRacaIdRaca(int racaIdRaca) {
        this.racaIdRaca = racaIdRaca;
    }

    public Integer getFornecedorIdFornecedor() {
        return fornecedorIdFornecedor;
    }

    public void setFornecedorIdFornecedor(Integer fornecedorIdFornecedor) {
        this.fornecedorIdFornecedor = fornecedorIdFornecedor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

