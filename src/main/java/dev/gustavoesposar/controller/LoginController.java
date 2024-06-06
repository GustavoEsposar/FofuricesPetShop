package dev.gustavoesposar.controller;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dev.gustavoesposar.App;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.utils.AutenticacaoSenha;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public final class LoginController {
    private final String SQL_CONSULTAR_SENHA = "SELECT senha FROM login WHERE email = ?";
    private final String SQL_ATUALIZAR_DATA_ACESSO = "UPDATE login SET data_acesso = ? WHERE email = ?";

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

        try(ResultSet res = DatabaseManager.executarConsulta(SQL_CONSULTAR_SENHA, email)) {

            if(res.next() && AutenticacaoSenha.autenticarSenha(senha, res.getString("senha"))) {
                    atualizarDataAcesso(email);
                    atualizarSceneMenu();
            } else {
                throw new IllegalArgumentException("Email ou senha incorretos");
            }
            DatabaseManager.fecharConexao();
        } catch(Exception e) {
            janelaDeErro(e.toString());
        }
    }

    private void atualizarDataAcesso(String email) throws SQLException {
        String dataAcesso = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DatabaseManager.executarUpdate(SQL_ATUALIZAR_DATA_ACESSO, dataAcesso, email);
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
