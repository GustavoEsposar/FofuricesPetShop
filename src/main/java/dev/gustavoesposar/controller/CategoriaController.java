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
    private final String SQL_INSERT = "INSERT INTO categoria (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nome = ?);";
    private final String SQL_SELECT = "SELECT * FROM Categoria ORDER BY nome";
    private final String SQL_DELETE = "DELETE FROM Categoria WHERE idCategoria = ? LIMIT 1;";

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
        try {
            String nome = txtAdd.getText();
            if (DatabaseManager.executarUpdate(SQL_INSERT, nome, nome)) {
                atualizarTabela();
            }
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }
    }

    protected void atualizarTabela() {
        ObservableList<Categoria> categoriasList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(SQL_SELECT)) {

            while (resultSet.next()) {
                int idCategoria = resultSet.getInt("idCategoria");
                String nome = resultSet.getString("nome");

                Categoria categoria = new Categoria(idCategoria, nome);
                categoriasList.add(categoria);
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro(e.toString());
        }

        tblCategorias.setItems(categoriasList);
    }

    @FXML
    private void removerCategoria() {
        try {
            String id = txtRm.getText();
            if (DatabaseManager.executarUpdate(SQL_DELETE, id)) {
                atualizarTabela();
            }
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
