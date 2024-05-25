package dev.gustavoesposar.model;

public final class Pet {

    private int idPet;
    private String especieNome;
    private String racaNome;
    private String fornecedorNome;
    private double valor;

    public Pet() {
    }

    public Pet(int idPet, String especieNome, String racaNome, String fornecedorNome, double valor) {
        this.idPet = idPet;
        this.especieNome = especieNome;
        this.racaNome = racaNome;
        this.fornecedorNome = fornecedorNome;
        this.valor = valor;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}

