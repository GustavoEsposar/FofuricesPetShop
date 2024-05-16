package dev.gustavoesposar.controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.App;
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
                switchToMainWindow();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de login");
                alert.setHeaderText("Falha no login");
                alert.setContentText("Credenciais inválidas. Verifique seu email e senha.");
                alert.showAndWait();
            }

        } catch(SQLException e) {
            janelaErroDeConexao();
        }
    }

    private void janelaErroDeConexao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de conexão");
        alert.setHeaderText("Banco de dados não encontrado");
        alert.setContentText("Não foi possivel conectar ao banco de dados.");
        alert.showAndWait();
    }

    private void switchToMainWindow() {
        try {
            App.setNewScene("principal", "Gerenciamento do Sistema");
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }
}
