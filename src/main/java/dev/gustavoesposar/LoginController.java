package dev.gustavoesposar;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnLogin;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    public void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        // Lógica de verificação de login
        if (email.equals("email@dominio.com") && senha.equals("senha123")) {
            System.out.println("Login bem-sucedido!");
            // Adicione aqui o código para redirecionar para a próxima tela ou exibir uma
            // mensagem de sucesso
        } else {
            System.out.println("Falha no login. Verifique suas credenciais.");
            // Adicione aqui o código para exibir uma mensagem de erro ao usuário
        }
    }
}
