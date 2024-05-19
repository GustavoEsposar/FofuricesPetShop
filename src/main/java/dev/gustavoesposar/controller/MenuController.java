package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    private Button btnAnimais;

    @FXML
    private Button btnEstoque;

    @FXML
    private Button btnFornecedores;

    @FXML
    void atualizarSceneAnimais() {
        try {
            App.setNewScene("especies", "Gerenciamento de Espécies");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void atualizarSceneFornecedores() {

    }

    @FXML
    void atualizarSceneEstoque() {

    }

    @FXML
    void atualizarSceneProdutos() {

    }
}
