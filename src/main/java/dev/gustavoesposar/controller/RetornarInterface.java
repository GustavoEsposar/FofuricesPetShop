package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;

public interface RetornarInterface {
    @FXML
    default void voltarMenu() {
        try {
            App.setNewScene("menu", "Menu de Gerenciamento");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
