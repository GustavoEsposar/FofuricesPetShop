package dev.gustavoesposar.model;

public class Marca {
    private int idMarca;
    private String Nome;

    public Marca() { }

    public Marca(int idMarca, String Nome) {
        this.idMarca = idMarca;
        this.Nome = Nome;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }
}
