package dev.gustavoesposar.controller;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LoginsController extends OpcaoDoMenu{
    private final String sqlDelete = "DELETE FROM Login WHERE idLogin = ? LIMIT 1;";

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Login, String> colEmail;

    @FXML
    private TableColumn<Login, Integer> colIdLogin;

    @FXML
    private TableView<Login> tbl;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private void adicionar(ActionEvent event) {

    }

    @FXML
    private void atualizar(ActionEvent event) {

    }

    @FXML
    private void remover(ActionEvent event) {
        String id = txtId.getText();

        boolean sucesso = DatabaseManager.executarUpdate(sqlDelete, id);

        processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @Override
    protected void atualizarTabela() {

    }

    @Override
    protected void restaurarValoresVariaveis() {
        txtEmail.setText(null);
        txtSenha.setText(null);
        txtId.setText(null);
    }

    @FXML
    public void initialize() {
        configurarColunasTableView(); 
        ajustarLarguraColunas();
        atualizarTabela();
    }
    
    private void configurarColunasTableView() {
        colIdLogin.setCellValueFactory(new PropertyValueFactory<>("idLogin"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    private void ajustarLarguraColunas() {
        colIdLogin.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
        colEmail.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
    }

}
