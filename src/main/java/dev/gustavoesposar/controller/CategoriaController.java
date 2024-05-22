package dev.gustavoesposar.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Categoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CategoriaController implements RetornarInterface {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private TableColumn<Categoria, Integer> colIdCategoria;

    @FXML
    private TableColumn<Categoria, String> colNome;

    @FXML
    private TableView<Categoria> tblCategorias;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtRm;

    @FXML
    void voltarMenu(ActionEvent event) {
        RetornarInterface.super.voltarMenu();
    }

    @FXML
    private void adicionarCategoria() {
        String nome = txtAdd.getText();
        String sql = "INSERT INTO categoria (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = ?);";

        boolean sucesso = executarUpdate(sql, nome, nome);

        if (sucesso) {
            atualizarTabela();
        } else {
            janelaErroDeInsercao();
        }
    }

    private boolean executarUpdate(String sql, String... params) {
        try {
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            int res = statement.executeUpdate();
            DatabaseManager.fecharConexao();
            return res > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void atualizarTabela() {
        ObservableList<Categoria> categoriasList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Categoria";
        try (Connection conn = DatabaseManager.getConexao();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idCategoria = resultSet.getInt("idCategoria");
                String nome = resultSet.getString("nome");

                Categoria categoria = new Categoria(idCategoria, nome);
                categoriasList.add(categoria);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblCategorias.setItems(categoriasList);
    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("O nome fornecido já existe no banco de dados");
        alert.showAndWait();
    }

    @FXML
    private void removerCategoria() {
        String id = txtRm.getText();
        String sql = "DELETE FROM Categoria WHERE idCategoria = ? LIMIT 1;";

        boolean sucesso = executarUpdate(sql, id);

        if (sucesso) {
            atualizarTabela();
        } else {
            janelaErroDeInsercao();
        }
    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTabela();
    }
    
    private void configurarColunasTableView() {
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }
    
    private void ajustarLarguraColunas() {
        colIdCategoria.prefWidthProperty().bind(tblCategorias.widthProperty().multiply(0.5));
        colNome.prefWidthProperty().bind(tblCategorias.widthProperty().multiply(0.5));
    }

}
