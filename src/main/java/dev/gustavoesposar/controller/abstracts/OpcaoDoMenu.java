package dev.gustavoesposar.controller.abstracts;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public abstract class OpcaoDoMenu {
    @FXML
    protected void voltarMenu() {
        try {
            App.setNewScene("menu", "Menu de Gerenciamento");
        } catch (IOException e) {
            janelaDeErro("Erro ao voltar para o Menu princiapl");
        }
    }

    protected abstract void atualizarTabela();

    protected void janelaDeErro(String erro) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(erro);
        alert.setWidth(300);
        alert.showAndWait();
    }

    protected abstract void restaurarValoresVariaveis();
}
