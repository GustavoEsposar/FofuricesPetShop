package dev.gustavoesposar.controller;

import java.io.IOException;

import dev.gustavoesposar.App;
import javafx.fxml.FXML;

public abstract class OpcaoDoMenu {
    @FXML
    protected void voltarMenu() {
        try {
            App.setNewScene("menu", "Menu de Gerenciamento");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
