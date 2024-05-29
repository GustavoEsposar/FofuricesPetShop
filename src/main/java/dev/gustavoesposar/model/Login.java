package dev.gustavoesposar.model;

public class Login {
    private int idLogin;
    private String email;

    public Login() {
    }

    public Login(int idLogin, String email) {
        this.idLogin = idLogin;
        this.email = email;
    }

    public int getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(int idLogin) {
        this.idLogin = idLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
