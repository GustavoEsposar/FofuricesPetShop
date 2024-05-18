package dev.gustavoesposar.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.App;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Especie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AnimaisController implements RetornarInterface{

    @FXML
    private Button btnAddEspecie;

    @FXML
    private Button btnRmEspecie;

    @FXML
    private Tab tabEspecies;

    @FXML
    private Tab tabPets;

    @FXML
    private Tab tabRacas;

    @FXML
    private TableView<Especie> tblEspecies;

    @FXML
    private TableColumn<Especie, Integer> colIdEspecie;

    @FXML
    private TableColumn<Especie, String> colNomeEspecie;

    @FXML
    private TextField txtAddEspecie;

    @FXML
    private TextField txtRmEspecie;

    @FXML
    private void adicionarEspecie(ActionEvent event) {
        String nomeEspecie = txtAddEspecie.getText();
        String sql = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?);";
        
        boolean sucesso = executarUpdate(sql, nomeEspecie, nomeEspecie);
        
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
        
        boolean sucesso = executarUpdate(sql, idEspecie);
        
        if (sucesso) {
            atualizarTabela();
        } else {
            janelaErroDeInsercao();
        }
    }

    @Override
    @FXML
    public void voltarMenu() {
        RetornarInterface.super.voltarMenu();
    }

    @FXML
    public void initialize() {
        // Configurando as colunas do TableView
        colIdEspecie.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeEspecie.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Ajustar largura das colunas
        colIdEspecie.prefWidthProperty().bind(tblEspecies.widthProperty().multiply(0.5));
        colNomeEspecie.prefWidthProperty().bind(tblEspecies.widthProperty().multiply(0.5));
    
        atualizarTabela();
    }   

}
