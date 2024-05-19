package dev.gustavoesposar.model;

public class Raca {
    private int idRaca;
    private String nome;
    private String nomeEspecie; // Alteração do tipo e nome do atributo

    public Raca() { }

    public Raca(int idRaca, String nome, String nomeEspecie) { // Alteração do construtor
        this.idRaca = idRaca;
        this.nome = nome;
        this.nomeEspecie = nomeEspecie;
    }

    public int getIdRaca() {
        return idRaca;
    }

    public void setIdRaca(int idRaca) {
        this.idRaca = idRaca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeEspecie() { // Getter para nomeEspecie
        return nomeEspecie;
    }

    public void setNomeEspecie(String nomeEspecie) { // Setter para nomeEspecie
        this.nomeEspecie = nomeEspecie;
    }

    @Override
    public String toString() {
        return "Raca{" +
                "idRaca=" + idRaca +
                ", nome='" + nome + '\'' +
                ", nomeEspecie='" + nomeEspecie + '\'' + // Atualização para nomeEspecie
                '}';
    }
}
