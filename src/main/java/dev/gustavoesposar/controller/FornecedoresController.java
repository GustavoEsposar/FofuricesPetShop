package dev.gustavoesposar.controller;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Fornecedor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FornecedoresController extends OpcaoDoMenu{
    private final String sqlDelete = "DELETE FROM Fornecedor WHERE idFornecedor = ? LIMIT 1;";

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Fornecedor, String> colCnpj;

    @FXML
    private TableColumn<Fornecedor, String> colEmail;

    @FXML
    private TableColumn<Fornecedor, Integer> colIdFornecedor;

    @FXML
    private TableColumn<Fornecedor, String> colNomeFantasia;

    @FXML
    private TableColumn<Fornecedor, String> colRazaoSocial;

    @FXML
    private TableColumn<Fornecedor, String> colTelefone;

    @FXML
    private TableView<Fornecedor> tbl;

    @FXML
    private TextField txtCnpj;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNomeFantasia;

    @FXML
    private TextField txtRazaoSocial;

    @FXML
    private TextField txtTelefone;

    @FXML
    private void adicionar(ActionEvent event) {

    }

    @FXML
    private void atualizar(ActionEvent event) {

    }

    @FXML
    private void remover(ActionEvent event) {
        String idSelecionado = txtId.getText();

        boolean sucesso = DatabaseManager.executarUpdate(sqlDelete, idSelecionado);

        super.processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @FXML
    private void voltarMenu(ActionEvent event) {

    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTabela();
    }

    private void configurarColunasTableView() {
        colIdFornecedor.setCellValueFactory(new PropertyValueFactory<>("idFornecedor"));
        colNomeFantasia.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
        colRazaoSocial.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCnpj.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
    }

    private void ajustarLarguraColunas() {
        colIdFornecedor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.1));
        colNomeFantasia.prefWidthProperty().bind(tbl.widthProperty().multiply(0.18));
        colRazaoSocial.prefWidthProperty().bind(tbl.widthProperty().multiply(0.18));
        colEmail.prefWidthProperty().bind(tbl.widthProperty().multiply(0.18));
        colTelefone.prefWidthProperty().bind(tbl.widthProperty().multiply(0.18));
        colCnpj.prefWidthProperty().bind(tbl.widthProperty().multiply(0.18));
    }

    @Override
    protected void atualizarTabela() {
;
    }

    @Override
    protected void restaurarValoresVariaveis() {

    }

}
