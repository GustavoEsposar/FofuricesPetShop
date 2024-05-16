package dev.gustavoesposar.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Especie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PrincipalController {

    @FXML
    protected Button btnAddEspecie;

    @FXML
    protected Button btnRmEspecie;

    @FXML
    protected Tab tabCategorias;

    @FXML
    protected Tab tabEspecies;

    @FXML
    protected Tab tabEstoque;

    @FXML
    protected Tab tabFornecedores;

    @FXML
    protected Tab tabMarcas;

    @FXML
    protected Tab tabPets;

    @FXML
    protected Tab tabProdutos;

    @FXML
    protected Tab tabRacas;

    @FXML
    protected TableView<Especie> tblEspecies;

    @FXML
    protected TextField txtAddEspecie;

    @FXML
    protected TextField txtRmEspecie;

    @FXML
    private ObservableList<Especie> especiesList = FXCollections.observableArrayList();

    @FXML
    public void adicionarEspecie() {
        String nomeEspecie = txtAddEspecie.getText();
    
        try {
            // adiciona apenas se não existir registro
            String sql = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?)";
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            statement.setString(1, nomeEspecie);
            statement.setString(2, nomeEspecie);
            statement.executeUpdate();

            atualizarTabela();
        } catch (SQLException e) {
            janelaErroDeConexao();
        }
    }    

    @FXML
    public void removerEspecie() {

    }

    private void atualizarTabela() {
        try {
            tblEspecies.getItems().clear();
            String sql = "SELECT * FROM Especie";
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            ResultSet res = statement.executeQuery();

            while (res.next()) {
                // Criar um objeto Especie (supondo que exista uma classe Especie com os atributos correspondentes)
                Especie especie = new Especie();
                especie.setId(res.getInt("idEspecie"));
                especie.setNome(res.getString("nome"));
                // Adicionar o objeto à tabela na interface gráfica
                especiesList.add(especie);
            }
            System.out.println(especiesList);
            
            tblEspecies.setItems(especiesList);
            tblEspecies.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
            //janelaErroDeConexao();
        }
    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("Falha ao inserir no banco de dados");
        alert.showAndWait();
    }

    protected void janelaErroDeConexao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de conexão");
        alert.setHeaderText("Banco de dados não encontrado");
        alert.setContentText("Não foi possivel conectar ao banco de dados.");
        alert.showAndWait();
    }
}
