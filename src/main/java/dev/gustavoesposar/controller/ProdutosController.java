package dev.gustavoesposar.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProdutosController {

    @FXML
    private ChoiceBox<?> boxCategoria;

    @FXML
    private ChoiceBox<?> boxMarca;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colIdProduto;

    @FXML
    private TableColumn<?, ?> colMarca;

    @FXML
    private TableColumn<?, ?> colNome;

    @FXML
    private TableColumn<?, ?> colPreco;

    @FXML
    private TableView<?> tbl;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private void adicionar(ActionEvent event) {

    }

    @FXML
    private void atualizar(ActionEvent event) {

    }

    @FXML
    private void remover(ActionEvent event) {

    }

    @FXML
    private void voltarMenu(ActionEvent event) {

    }

}
