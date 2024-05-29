package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Login;
import dev.gustavoesposar.utils.PasswordUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private final String sqlSelect = "SELECT idLogin, email FROM login;";
    private final String sqlInsert =    "INSERT INTO login (email, senha)\r\n" + //
                                        "SELECT ?, ? FROM DUAL\r\n" + //
                                        "WHERE NOT EXISTS (\r\n" + //
                                        "    SELECT 1 FROM login WHERE email = ?\r\n" + //
                                        ")\r\n" + //
                                        "";

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
        String email = txtEmail.getText();
        String senha = PasswordUtil.hashPassword(txtSenha.getText());

        boolean sucesso = false;
        if (btnAdd.getText().equals("Update")) {
            btnAdd.setText("Adicionar");
            //sucesso = DatabaseManager.executarUpdate(sqlUpdatePet, valor, raca, fornecedor, txtId.getText());
        } else {
            sucesso = DatabaseManager.executarUpdate(sqlInsert, email, senha, email);
        }

        processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @FXML
    private void atualizar(ActionEvent event) {
        if (btnAdd.getText().equals("Update")) {
            return;
        }

        btnAdd.setText("Update");
        
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
        ObservableList<Login> loginsList = FXCollections.observableArrayList();

        try(ResultSet res = DatabaseManager.executarConsulta(sqlSelect)) {
            while (res.next()) {
                int id = res.getInt("idLogin");
                String email = res.getString("email");

                Login login = new Login(id, email);
                loginsList.add(login);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        tbl.setItems(loginsList);
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
