package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Marca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public final class MarcasController extends OpcaoDoMenu {
    private final String sqlSelect = "SELECT * FROM Marca";
    private final String sqlInsert =  "INSERT INTO Marca (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Marca WHERE nome = ?);";
    private final String sqlDelete = "DELETE FROM Marca WHERE idMarca = ? LIMIT 1;";

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private TableColumn<Marca, Integer> colIdMarca;

    @FXML
    private TableColumn<Marca, String> colNome;

    @FXML
    private TableView<Marca> tbl;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtRm;

    @FXML
    private void adicionar(ActionEvent event) {
        String nome = txtAdd.getText();

        boolean sucesso = DatabaseManager.executarUpdate(sqlInsert, nome, nome);

        super.processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Marca> marcasList = FXCollections.observableArrayList();

        try(ResultSet res = DatabaseManager.executarConsulta(sqlSelect)){
            while (res.next()) {
                int idMarca = res.getInt("idMarca");
                String nome = res.getString("Nome");

                Marca marca = new Marca(idMarca, nome);
                marcasList.add(marca);
            }

            DatabaseManager.fecharConexao();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        tbl.setItems(marcasList);
    }

    @FXML
    private void remover(ActionEvent event) {
        String idMarca = txtRm.getText();

        boolean sucesso = DatabaseManager.executarUpdate(sqlDelete, idMarca);

        super.processarResultado(sucesso);
    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        this.atualizarTabela();
    }

    private void configurarColunasTableView() {
        colIdMarca.setCellValueFactory(new PropertyValueFactory<>("idMarca"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
    }

    private void ajustarLarguraColunas() {
        colIdMarca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
        colNome.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
    }

    @Override
    protected void restaurarValoresVariaveis() {
        txtAdd.setText(null);
    }
}
