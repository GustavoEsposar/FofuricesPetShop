package dev.gustavoesposar.model;

public class Raca {
    private int idRaca;
    private String nome;

    public Raca() { }

    public Raca(int idRaca, String nome) {
        this.idRaca = idRaca;
        this.nome = nome;
    }

    public int getId() {
        return idRaca;
    }

    public void setId(int idRaca) {
        this.idRaca = idRaca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Raça{" +
                "idRaca=" + idRaca +
                ", nome='" + nome + '\'' +
                '}';
    }
}
