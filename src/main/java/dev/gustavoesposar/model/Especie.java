package dev.gustavoesposar.model;

public final class Especie {
    private int idEspecie;
    private String nome;

    public Especie() { }

    public Especie(int idEspecie, String nome) {
        this.idEspecie = idEspecie;
        this.nome = nome;
    }

    public int getId() {
        return idEspecie;
    }

    public void setId(int idEspecie) {
        this.idEspecie = idEspecie;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
