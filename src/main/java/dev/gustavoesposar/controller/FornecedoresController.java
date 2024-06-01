package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Fornecedor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FornecedoresController extends OpcaoDoMenu{
    private final String sqlDelete = "DELETE FROM Fornecedor WHERE idFornecedor = ? LIMIT 1;";
    private final String sqlInsert = "INSERT INTO Fornecedor (nomeFantasia, razaoSocial, email, telefone, cnpj) " +
                                    "SELECT ?, ?, ?, ?, ? " +
                                    "WHERE NOT EXISTS (SELECT 1 FROM Fornecedor WHERE cnpj = ?)";
    private final String sqlSelect =    "select " + //
                                        "    idFornecedor, " +
                                        "    nomeFantasia, " + //
                                        "    razaoSocial,  " + //
                                        "    email, " + //
                                        "    telefone, " + //
                                        "    cnpj" + //
                                        " from fornecedor";
    private final String sqlUpdate =    "UPDATE fornecedor\n" + //
                                        "SET\n" + //
                                        "\tnomeFantasia = ?,\n" + //
                                        "    razaoSocial = ?,\n" + //
                                        "    email = ?,\n" + //
                                        "    telefone = ?,\n" + //
                                        "    cnpj = ?\n" + //
                                        "WHERE idFornecedor = ?";

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
        try {
            String fantasia = txtNomeFantasia.getText();
            String razao = txtRazaoSocial.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String cnpj = txtCnpj.getText();

            if (btnAdd.getText().equals("Update")) {
                btnAdd.setText("Adicionar");
                DatabaseManager.executarUpdate(sqlUpdate, fantasia, razao, email, telefone, cnpj, txtId.getText());
            } else {
                DatabaseManager.executarUpdate(sqlInsert, fantasia, razao, email, telefone, cnpj, cnpj);
            }
            atualizarTabela();
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos corretamente");
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }      
    }

    @FXML
    private void atualizar(ActionEvent event) {
        if (btnAdd.getText().equals("Update")) {
            return;
        }

        btnAdd.setText("Update");
        String sql = sqlSelect.replace("idFornecedor,", "").concat(" WHERE idFornecedor = " + txtId.getText());
        try(ResultSet res = DatabaseManager.executarConsulta(sql)) {
            if (res.next()) {
                String fantasia = res.getString("nomeFantasia");
                String razao = res.getString("razaoSocial");
                String email = res.getString("email");
                String telefone = res.getString("telefone");
                String cnpj = res.getString("cnpj");

                txtNomeFantasia.setText(fantasia);
                txtRazaoSocial.setText(razao);
                txtEmail.setText(email);
                txtTelefone.setText(telefone);
                txtCnpj.setText(cnpj);  
            }
        } catch(SQLException e) {
            janelaDeErro("Erro de comunicação com o banco de dados");
        }
    }

    @FXML
    private void remover(ActionEvent event) {
        try {
            DatabaseManager.executarUpdate(sqlDelete, txtId.getText());
            atualizarTabela();
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Fornecedor> fornList = FXCollections.observableArrayList();

        try(ResultSet res = DatabaseManager.executarConsulta(sqlSelect)) {
            while (res.next()) {
                int id = res.getInt("idFornecedor");
                String fantasia = res.getString("nomeFantasia");
                String razao = res.getString("razaoSocial");
                String email = res.getString("email");
                String telefone = res.getString("telefone");
                String cnpj = res.getString("cnpj");
    
                Fornecedor fornecedor = new Fornecedor(id, fantasia, razao, email, telefone, cnpj);
                fornList.add(fornecedor);
            }
        } catch(SQLException e) {
            janelaDeErro("Problema ao comunicar-se com o banco de dados para atualizar a tabela");
        }
        tbl.setItems(fornList);
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
    protected void restaurarValoresVariaveis() {
        txtNomeFantasia.setText(null);
        txtRazaoSocial.setText(null);
        txtEmail.setText(null);
        txtTelefone.setText(null);
        txtCnpj.setText(null);
        txtId.setText(null);
    }

}
