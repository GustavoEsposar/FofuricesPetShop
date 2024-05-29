package dev.gustavoesposar.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.App;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.utils.PasswordUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public final class LoginController {
    private final String sqlConsultarSenha = "SELECT senha FROM login WHERE email = ?";

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private Button btnLogin;

    @FXML
    public void verificarLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        try(ResultSet res = DatabaseManager.executarConsulta(sqlConsultarSenha, email)) {

            if(res.next() & PasswordUtil.checkPassword(senha, res.getString("senha"))) {
                    atualizarSceneMenu();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro de login");
                alert.setHeaderText("Falha no login");
                alert.setContentText("Credenciais inválidas. Verifique seu email e senha.");
                alert.showAndWait();
            }
            DatabaseManager.fecharConexao();
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

    private void atualizarSceneMenu() {
        try {
            App.setNewScene("menu", "Menu de Gerenciamento");
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }
}
