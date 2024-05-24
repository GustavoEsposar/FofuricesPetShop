package dev.gustavoesposar.controller;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public final class PetsController extends OpcaoDoMenu {
    private final String sqlInsert = "";
    private final String sqlSelect = "";
    private final String sqlDelete = "";

    @FXML
    private ChoiceBox<String> boxEspecie;

    @FXML
    private ChoiceBox<String> boxFornecedor;

    @FXML
    private ChoiceBox<String> boxRaca;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<String, String> colEspecie;

    @FXML
    private TableColumn<String, String> colFornecedor;

    @FXML
    private TableColumn<String, String> colIdPet;

    @FXML
    private TableColumn<String, String> colRaca;

    @FXML
    private TableColumn<String, String> colValor;

    @FXML
    private TableView<String> tbl;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtId;

    @FXML
    void adicionar(ActionEvent event) {

    }

    @FXML
    void atualizar(ActionEvent event) {

    }

    @FXML
    void remover(ActionEvent event) {

    }

    @Override
    protected void atualizarTabela() {

    }

    @Override
    protected void restaurarValoresVariaveis() {

    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTabela();
    }

    private void configurarColunasTableView() {
        //colIdRaca.setCellValueFactory(new PropertyValueFactory<>("idRaca"));
        //colEspecie.setCellValueFactory(new PropertyValueFactory<>("nomeEspecie"));
        //colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }

    private void ajustarLarguraColunas() {
        colIdPet.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colEspecie.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colRaca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colValor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colFornecedor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
    }

}
