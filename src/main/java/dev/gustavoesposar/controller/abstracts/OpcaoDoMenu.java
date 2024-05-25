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

    protected void processarResultado(boolean sucesso) {
        if (sucesso) {
            atualizarTabela();
        } else {
            janelaErroDeInsercao();
        }
    }

    protected abstract void atualizarTabela();

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("O nome fornecido já existe no banco de dados");
        alert.showAndWait();
    }

    protected abstract void restaurarValoresVariaveis();
}
