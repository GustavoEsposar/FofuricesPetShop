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

            if(res.next() && PasswordUtil.checkPassword(senha, res.getString("senha"))) {
                    atualizarSceneMenu();
            } else {
                throw new IllegalArgumentException("Email ou senha incorretos");
            }
            DatabaseManager.fecharConexao();
        } catch(Exception e) {
            janelaDeErro(e.toString());
        }
    }

    protected void janelaDeErro(String erro) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(erro);
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
