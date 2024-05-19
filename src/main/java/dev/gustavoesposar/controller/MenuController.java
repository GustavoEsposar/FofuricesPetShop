package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML
    private Button btnEspecies;

    @FXML
    private Button btnRacas;

    @FXML
    private Button btnEstoque;

    @FXML
    private Button btnFornecedores;

    @FXML
    private void atualizarSceneEspecies() {
        try {
            App.setNewScene("especies", "Gerenciamento de Espécies");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atualizarSceneRacas() {
        try {
            App.setNewScene("racas", "Gerenciamento de Raças");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atualizarSceneFornecedores() {

    }

    @FXML
    private void atualizarSceneEstoque() {

    }

    @FXML
    private void atualizarSceneProdutos() {

    }
}
