package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public final class MenuController {

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
    private void atualizarScenePets() {
        atualizarScene("pets", "Pets");
    }

    @FXML
    private void atualizarSceneFornecedores() {
        atualizarScene("fornecedores", "Fornecedores");
    }

    @FXML
    private void atualizarSceneEstoque() {

    }

    @FXML
    private void atualizarSceneCategorias() {
        atualizarScene("categorias", "Categorias");
    }

    @FXML
    private void atualizarSceneMarcas() {
        atualizarScene("marcas", "Marcas");
    }

    @FXML
    private void atualizarSceneProdutos() {

    }
}
