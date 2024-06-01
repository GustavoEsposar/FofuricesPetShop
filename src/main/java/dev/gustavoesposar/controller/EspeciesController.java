package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Especie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public final class EspeciesController extends OpcaoDoMenu{
    private final String sqlSelect = "SELECT * FROM Especie";
    private final String sqlInsert = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?);";
    private final String sqlDelete = "DELETE FROM Especie WHERE idEspecie = ? LIMIT 1;";

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

        try {
            String nomeEspecie = txtAddEspecie.getText();
            DatabaseManager.executarUpdate(sqlInsert, nomeEspecie, nomeEspecie);
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
        ObservableList<Especie> especiesList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sqlSelect)) {

            while (resultSet.next()) {
                int idEspecie = resultSet.getInt("idEspecie");
                String nome = resultSet.getString("nome");

                Especie especie = new Especie(idEspecie, nome);
                especiesList.add(especie);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter registros da tabela no banco");
        }

        tblEspecies.setItems(especiesList);
    }

    @FXML
    private void removerEspecie(ActionEvent event) {

        try {
            String idEspecie = txtRmEspecie.getText();
            DatabaseManager.executarUpdate(sqlDelete, idEspecie);
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

    @Override
    protected void restaurarValoresVariaveis() {
        txtAddEspecie.setText(null);
    }
}
