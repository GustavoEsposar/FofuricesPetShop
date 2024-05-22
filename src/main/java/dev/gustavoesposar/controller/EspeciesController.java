package dev.gustavoesposar.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Especie;
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

public class EspeciesController extends OpcaoDoMenu{

    @FXML
    private TextField txtAddEspecie;

    @FXML
    private Button btnAddEspecie;

    @FXML
    private TextField txtRmEspecie;

    @FXML
    private Button btnRmEspecie;

    @FXML
    private TableView<Especie> tblEspecies;

    @FXML
    private TableColumn<Especie, Integer> colIdEspecie;

    @FXML
    private TableColumn<Especie, String> colNomeEspecie;

    @FXML
    private void adicionarEspecie(ActionEvent event) {
        String nomeEspecie = txtAddEspecie.getText();
        String sql = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?);";
        
        boolean sucesso = DatabaseManager.executarUpdate(sql, nomeEspecie, nomeEspecie);
        
        if (sucesso) {
            atualizarTabela();
        } else {
            janelaErroDeInsercao();
        }
    }
    
    private void atualizarTabela() {
        ObservableList<Especie> especiesList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Especie";
        try (Connection conn = DatabaseManager.getConexao();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idEspecie = resultSet.getInt("idEspecie");
                String nome = resultSet.getString("nome");

                Especie especie = new Especie(idEspecie, nome);
                especiesList.add(especie);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblEspecies.setItems(especiesList);
    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("O nome fornecido já existe no banco de dados");
        alert.showAndWait();
    }

    @FXML
    private void removerEspecie(ActionEvent event) {
        String idEspecie = txtRmEspecie.getText();
        String sql = "DELETE FROM Especie WHERE idEspecie = ? LIMIT 1;";
        
        boolean sucesso = DatabaseManager.executarUpdate(sql, idEspecie);
        
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
        colIdEspecie.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeEspecie.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }
    
    private void ajustarLarguraColunas() {
        colIdEspecie.prefWidthProperty().bind(tblEspecies.widthProperty().multiply(0.5));
        colNomeEspecie.prefWidthProperty().bind(tblEspecies.widthProperty().multiply(0.5));
    }
}
