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

    private void atualizarScene(String fxml, String titulo) {
        try {
            App.setNewScene(fxml, "Gerenciamento de " + titulo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void atualizarSceneEspecies() {
        atualizarScene("especies", "Espécies");
    }

    @FXML
    private void atualizarSceneRacas() {
        atualizarScene("racas", "Raças");
    }

    @FXML
    private void atualizarSceneFornecedores() {

    }

    @FXML
    private void atualizarSceneEstoque() {

    }

    @FXML
    private void atualizarSceneCategorias() {
        atualizarScene("categorias", "Categorias");
    }

    @FXML
    private void atualizarSceneProdutos() {

    }
}
