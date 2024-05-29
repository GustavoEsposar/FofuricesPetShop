package dev.gustavoesposar.model;

public class Fornecedor {
    private int idFornecedor;
    private String nomeFantasia;
    private String razaoSocial;
    private String email;
    private String telefone;
    private String cnpj;

    public Fornecedor() { }

    public Fornecedor(int id, String fantasia, String razao, String email, String telefone, String cnpj) {
        this.idFornecedor = id;
        this.nomeFantasia = fantasia;
        this.razaoSocial = razao;
        this.email = email;
        this.telefone = telefone;
        this.cnpj = cnpj;
    }

    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
