package dev.gustavoesposar.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Produto;
import dev.gustavoesposar.model.Raca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProdutosController extends OpcaoDoMenu {
    private final String CATEGORIA_DEFAULT = "Selecionar Categoria";
    private final String MARCA_DEFAULT = "Selecionar Marca";
    private final String SQL_DELETE = "DELETE FROM Produto WHERE idProduto = ? LIMIT 1;";
    private final String SQL_SELECT_PRODUTO = "SELECT \n" + //
                "\tidProduto,\n" + //
                "    categoria.nome 'categoria',\n" + //
                "    marca.nome 'marca',\n" + //
                "    produto.nome 'nome',\n" + //
                "    precoUnitario 'preco'\n" + //
                "FROM produto\n" + //
                "JOIN categoria on categoria.idCategoria = produto.Categoria_idCategoria\n" + //
                "JOIN marca on marca.idMarca = produto.Marca_idMarca";

    @FXML
    private ChoiceBox<String> boxCategoria;

    @FXML
    private ChoiceBox<String> boxMarca;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Produto, String> colCategoria;

    @FXML
    private TableColumn<Produto, Integer> colIdProduto;

    @FXML
    private TableColumn<Produto, String> colMarca;

    @FXML
    private TableColumn<Produto, String> colNome;

    @FXML
    private TableColumn<Produto, BigDecimal> colPreco;

    @FXML
    private TableView<Produto> tbl;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private void adicionar(ActionEvent event) {

    }

    @FXML
    private void atualizar(ActionEvent event) {
        
    }

    @FXML
    private void remover(ActionEvent event) {
        try {
            String id = txtId.getText();
            DatabaseManager.executarUpdate(SQL_DELETE, id);
            atualizarTabela();
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Produto> produtosList = FXCollections.observableArrayList();

        try (ResultSet res = DatabaseManager.executarConsulta(SQL_SELECT_PRODUTO)) {
            while (res.next()) {
                int id = res.getInt("idProduto");
                String cat = res.getString("categoria");
                String marca = res.getString("marca");
                String nome = res.getString("nome");
                BigDecimal preco = res.getBigDecimal("preco");
                
                produtosList.add(new Produto(id, cat, marca, nome, preco));
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter registros no banco");
        }

        tbl.setItems(produtosList);
    }

    @Override
    protected void restaurarValoresVariaveis() {
        boxCategoria.setValue(CATEGORIA_DEFAULT);
        boxMarca.setValue(MARCA_DEFAULT);
        txtNome.setText(null);
        txtPreco.setText(null);
        txtId.setText(null);
    }

        @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        //atualizarTodasChoiceBox();
        atualizarTabela();
    }

    private void configurarColunasTableView() {
        colIdProduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
    }

    private void ajustarLarguraColunas() {
        colIdProduto.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colCategoria.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colMarca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colNome.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colPreco.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
    }

}
