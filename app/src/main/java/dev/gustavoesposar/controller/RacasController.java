package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Especie;
import dev.gustavoesposar.model.Raca;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public final class RacasController extends OpcaoDoMenu {
    private final String SQL_SELECT = "SELECT Raca.idRaca, Raca.nome, Especie.nome AS nomeEspecie " +
            "FROM Raca " +
            "INNER JOIN Especie ON Raca.Especie_idEspecie = Especie.idEspecie ORDER BY Especie.nome, Raca.nome";
    private final String SQL_INSERT = "INSERT INTO Raca (nome, Especie_idEspecie) " +
            "SELECT ?, idEspecie FROM Especie " +
            "WHERE nome = ? " +
            "AND NOT EXISTS (SELECT 1 FROM Raca WHERE nome = ?)";
    private final String SQL_DELETE = "DELETE FROM Raca WHERE idRaca = ? LIMIT 1;";

    @FXML
    private ChoiceBox<String> boxEspecies;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private TableColumn<Especie, String> colEspecie;

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
        try {
            String nome = txtAdd.getText();
            String especieSelecionada = boxEspecies.getValue();
            DatabaseManager.executarUpdate(SQL_INSERT, nome, especieSelecionada, nome);
            atualizarTabela();
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos corretamente");
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }
    }

    protected void atualizarTabela() {
        ObservableList<Raca> racasList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(SQL_SELECT)) {

            while (resultSet.next()) {
                int idRaca = resultSet.getInt("idRaca");
                String nome = resultSet.getString("nome");
                String nomeEspecie = resultSet.getString("nomeEspecie");

                Raca raca = new Raca(idRaca, nome, nomeEspecie);
                racasList.add(raca);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter registros no banco");
        }

        tblRacas.setItems(racasList);
    }

    protected void restaurarValoresVariaveis() {
        boxEspecies.setValue("Espécie");
        txtAdd.setText(null);
    }

    @FXML
    private void removerRaca(ActionEvent event) {
        try {
            String idRaca = txtRm.getText();
            DatabaseManager.executarUpdate(SQL_DELETE, idRaca);
            atualizarEspecies();
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos corretamente");
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        } 
    }

    private void atualizarEspecies() {
        ObservableList<String> especiesList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta("SELECT nome FROM Especie ORDER BY nome")) {
            especiesList.add(0, "Espécie");
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                especiesList.add(nome);
            }

            Platform.runLater(() -> boxEspecies.setItems(especiesList));
            boxEspecies.setValue("Espécie");
            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter espécies do banco");
        }
    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTabela();
        atualizarEspecies();
    }

    private void configurarColunasTableView() {
        colIdRaca.setCellValueFactory(new PropertyValueFactory<>("idRaca"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("nomeEspecie"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    }

    private void ajustarLarguraColunas() {
        colIdRaca.prefWidthProperty().bind(tblRacas.widthProperty().multiply(0.33));
        colEspecie.prefWidthProperty().bind(tblRacas.widthProperty().multiply(0.33));
        colNome.prefWidthProperty().bind(tblRacas.widthProperty().multiply(0.33));
    }

}
