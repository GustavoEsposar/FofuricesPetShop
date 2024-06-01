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
            e.printStackTrace();
        }
    }

    protected abstract void atualizarTabela();

    protected void janelaDeErro(String erro) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(erro);
        alert.showAndWait();
    }

    protected abstract void restaurarValoresVariaveis();
}
