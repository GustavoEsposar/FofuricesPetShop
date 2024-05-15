package dev.gustavoesposar;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private void switchToSecondary() {
        try {
            App.setRoot("sucesso");
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        try {
            String sql = "SELECT * FROM login WHERE email = ? AND senha = ?";
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, senha);

            ResultSet res = statement.executeQuery();

            if(res.next()) {
                switchToSecondary();
            } else {
                // Adicione aqui o código para exibir uma mensagem de erro ao usuário
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de login");
                alert.setHeaderText("Falha no login");
                alert.setContentText("Credenciais inválidas. Verifique seu email e senha.");
                alert.showAndWait();
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
