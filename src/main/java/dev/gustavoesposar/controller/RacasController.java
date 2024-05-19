package dev.gustavoesposar.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Raca;
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

public class RacasController implements RetornarInterface {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private TableColumn<Raca, Integer> colIdRaca;

    @FXML
    private TableColumn<Raca, String> colNome;

    @FXML
    private TableView<Raca> tblRacas;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtRm;

    @FXML
    private void adicionarRaca(ActionEvent event) {
        String nome = txtAdd.getText();
        String sql = "INSERT INTO Raca (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Raca WHERE nome = ?);";

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
        ObservableList<Raca> racasList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM Raca";
        try (Connection conn = DatabaseManager.getConexao();
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int idRaca = resultSet.getInt("idRaca");
                String nome = resultSet.getString("nome");

                Raca raca = new Raca(idRaca, nome);
                racasList.add(raca);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tblRacas.setItems(racasList);
    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("O nome fornecido já existe no banco de dados");
        alert.showAndWait();
    }

    @FXML
    private void removerRaca(ActionEvent event) {
        String idRaca = txtRm.getText();
        String sql = "DELETE FROM Raca WHERE idRaca = ? LIMIT 1;";
        
        boolean sucesso = executarUpdate(sql, idRaca);
        
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
        colIdRaca.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }

    private void ajustarLarguraColunas() {
        colIdRaca.prefWidthProperty().bind(tblRacas.widthProperty().multiply(0.5));
        colNome.prefWidthProperty().bind(tblRacas.widthProperty().multiply(0.5));
    }

    @Override
    @FXML
    public void voltarMenu() {
        RetornarInterface.super.voltarMenu();
    }

}
