package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;

public final class MenuController {

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
    private void atualizarSceneLogin() {
        atualizarScene("logins", "Logins");
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
        atualizarScene("produtos", "Produtos");
    }

    @FXML
    private void atualizarSceneRelatorio() {
        //atualizarScene("relatorios", "Relatórios");
    }
}
