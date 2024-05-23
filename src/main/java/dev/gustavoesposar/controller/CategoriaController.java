package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Categoria;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public final class CategoriaController extends OpcaoDoMenu {

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
    private void adicionarCategoria() {
        String nome = txtAdd.getText();
        String sql = "INSERT INTO categoria (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = ?);";

        boolean sucesso = DatabaseManager.executarUpdate(sql, nome, nome);

        super.processarResultado(sucesso);
    }

    protected void atualizarTabela() {
        ObservableList<Categoria> categoriasList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Categoria";

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {

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

    @FXML
    private void removerCategoria() {
        String id = txtRm.getText();
        String sql = "DELETE FROM Categoria WHERE idCategoria = ? LIMIT 1;";

        boolean sucesso = DatabaseManager.executarUpdate(sql, id);

        super.processarResultado(sucesso);
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

    @Override
    protected void restaurarValoresVariaveis() {
        txtAdd.setText(null);
    }

}
