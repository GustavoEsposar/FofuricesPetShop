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
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AnimaisController {

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
    void adicionarEspecie(ActionEvent event) {
        String nomeEspecie = txtAddEspecie.getText();
        
        try {
            // adiciona apenas se não existir registro
            String sql = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?);\n";
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            statement.setString(1, nomeEspecie);
            statement.setString(2, nomeEspecie);

            int res = statement.executeUpdate();

            if (res > 0) {
                atualizarTabela();
            } else {
                janelaErroDeInsercao();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblEspecies.setItems(especiesList);
    }

    @FXML
    void removerEspecie(ActionEvent event) {

    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("O nome fornecido já existe no banco de dados");
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Configurando as colunas do TableView
        colIdEspecie.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeEspecie.setCellValueFactory(new PropertyValueFactory<>("nome"));
    
        atualizarTabela();
    }   

}
