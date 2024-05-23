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
        String sql = "INSERT INTO Marca (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Marca WHERE nome = ?);";

        boolean sucesso = DatabaseManager.executarUpdate(sql, nome, nome);

        super.processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Marca> marcasList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Marca";

        try(ResultSet res = DatabaseManager.executarConsulta(sql)){
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
        String sql = "DELETE FROM Marca WHERE idMarca = ? LIMIT 1;";

        boolean sucesso = DatabaseManager.executarUpdate(sql, idMarca);

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
