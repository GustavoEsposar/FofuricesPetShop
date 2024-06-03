package dev.gustavoesposar.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Produto;
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

public class ProdutosController extends OpcaoDoMenu {
    private static final String FORNECEDOR_DEFAULT = "Selecionar Fornecedor";
    private static final String CATEGORIA_DEFAULT = "Selecionar Categoria";
    private static final String MARCA_DEFAULT = "Selecionar Marca";
    private static final String SQL_DELETE = "DELETE FROM Produto WHERE idProduto = ? LIMIT 1;";
    private static final String SQL_SELECT_PRODUTO = "SELECT \n" + //
            "    idProduto,\n" + //
            "    categoria.nome 'categoria',\n" + //
            "    marca.nome 'marca',\n" + //
            "    produto.nome 'nome',\n" + //
            "    precoUnitario 'preco',\n" + //
            "    fornecedor.nomeFantasia 'Fornecedor',\n" + //
            "\tqtde,\n" + //
            "    qtdeMinima,\n" + //
            "    qtdeMaxima\n" + //
            "FROM produto\n" + //
            "JOIN categoria on categoria.idCategoria = produto.Categoria_idCategoria\n" + //
            "JOIN marca on marca.idMarca = produto.Marca_idMarca\n" + //
            "JOIN fornecedor on fornecedor.idFornecedor = produto.Fornecedor_idFornecedor\n" + //
            "Left JOIN estoque on estoque.Produto_idProduto = produto.idProduto";
    private static final String SQL_SELECT_CATEGORIA = "SELECT \n" + //
            "\tnome\n" + //
            "FROM categoria\n" + //
            "order by nome\n";

    private static final String SQL_SELECT_MARCA = "SELECT \n" + //
            "\tnome\n" + //
            "FROM marca\n" + //
            "order by nome\n";

    private static final String SQL_SELECT_FORNECEDOR = "SELECT \n" + //
            "\tnomeFantasia\n" + //
            "FROM Fornecedor\n" + //
            "order by nomeFantasia";

    private static final String SQL_INSERT = "INSERT INTO Produto (Categoria_idCategoria, Marca_idMarca, precoUnitario, nome, Fornecedor_idFornecedor)\n"
            + //
            "SELECT\n" + //
            "cat.idCategoria, \n" + //
            "marca.idMarca, \n" + //
            "?,\n" + //
            "?,\n" + //
            "forn.idFornecedor\n" + //
            "FROM \n" + //
            "(SELECT idCategoria FROM Categoria WHERE nome = ?) cat,\n" + //
            "(SELECT idMarca FROM Marca WHERE nome = ?) marca,\n" + //
            "(SELECT idFornecedor FROM Fornecedor WHERE nomeFantasia = ?) forn";

    private static final String SQL_UPDATE = "UPDATE produto p \n" + //
            "SET\n" + //
            "   p.precoUnitario = ?, \n" + //
            "   p.Marca_idMarca = (\n" + //
            "\t   SELECT idMarca\n" + //
            "\t   FROM marca \n" + //
            "\t   WHERE nome = ? \n" + //
            "   ),\n" + //
            "   p.Categoria_idCategoria = (\n" + //
            "\t\tSELECT idCategoria\n" + //
            "        from categoria\n" + //
            "        where nome = ?\n" + //
            "   ),\n" + //
            "   p.nome = ?,\n" + //
            "   p.Fornecedor_idFornecedor = ( \n" + //
            "\t   SELECT idFornecedor \n" + //
            "\t   FROM fornecedor \n" + //
            "\t   WHERE nomeFantasia = ? \n" + //
            "   )\n" + //
            "WHERE p.idProduto = ?";

    private static final String SQL_INSERT_ESTOQUE = "INSERT INTO Estoque (Produto_idProduto, qtde, qtdeMinima, qtdeMaxima)\n"
            + //
            "VALUES (?, ?, ?, ?);";

    private static final String SQL_UPDATE_ESTOQUE = "UPDATE estoque e\n" + //
            "SET\n" + //
            "\te.qtde = ?,\n" + //
            "    e.qtdeMinima = ?,\n" + //
            "    e.qtdeMaxima = ?\n" + //
            "WHERE e.Produto_idProduto = ?;";

    @FXML
    private ChoiceBox<String> boxCategoria;

    @FXML
    private ChoiceBox<String> boxMarca;

    @FXML
    private ChoiceBox<String> boxFornecedor;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Produto, Integer> colQtde;

    @FXML
    private TableColumn<Produto, Integer> colQtdeMinima;

    @FXML
    private TableColumn<Produto, Integer> colQtdeMaxima;

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
    private TableColumn<Produto, String> colFornecedor;

    @FXML
    private TableView<Produto> tbl;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtPreco;

    @FXML
    private TextField txtQtde;

    @FXML
    private TextField txtQtdeMinima;

    @FXML
    private TextField txtQtdeMaxima;

    @FXML
    private void adicionar(ActionEvent event) {
        String id = "";
        try {
            String cat = boxCategoria.getValue();
            String marca = boxMarca.getValue();
            String nome = txtNome.getText();
            BigDecimal preco = new BigDecimal(txtPreco.getText());
            String forn = boxFornecedor.getValue();
            String qtde = txtQtde.getText();
            String qtdeMin = txtQtdeMinima.getText();
            String qtdeMax = txtQtdeMaxima.getText();
            if (btnAdd.getText().equals("Update")) {
                DatabaseManager.executarUpdate(SQL_UPDATE_ESTOQUE, qtde, qtdeMin, qtdeMax, txtId.getText()); //ordem invertida para simular "ACID"
                DatabaseManager.executarUpdate(SQL_UPDATE, preco, marca, cat, nome, forn, txtId.getText());
                btnAdd.setText("Adicionar");
            } else {
                id = Integer.toString(DatabaseManager.executarUpdateLastId(SQL_INSERT, preco, nome, cat, marca, forn));
                DatabaseManager.executarUpdate(SQL_INSERT_ESTOQUE, id, qtde, qtdeMin, qtdeMax);
            }
            restaurarValoresVariaveis();
            atualizarTabela();
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos necessários corretamente!");
        } catch (Exception e) {
            janelaDeErro(e.toString());
        }
    }

    @FXML
    private void atualizar(ActionEvent event) {
        if (btnAdd.getText().equals("Update") | txtId.getText() == null) {
            return;
        }

        btnAdd.setText("Update");
        final String sql = SQL_SELECT_PRODUTO.replace("idProduto,", " ")
                .concat(" WHERE idProduto = " + txtId.getText());
        try (ResultSet res = DatabaseManager.executarConsulta(sql)) {
            if (res.next()) {
                boxFornecedor.setValue(res.getString("Fornecedor"));
                boxCategoria.setValue(res.getString("categoria"));
                boxMarca.setValue(res.getString("marca"));
                txtNome.setText(res.getString("nome"));
                txtPreco.setText(res.getString("preco"));
                txtQtde.setText(res.getString("qtde"));
                txtQtdeMinima.setText(res.getString("qtdeMinima"));
                txtQtdeMaxima.setText(res.getString("qtdeMaxima"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            janelaDeErro("Erro de comunicação com o banco de dados ao atualizar.");
        }
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
                String forn = res.getString("fornecedor");
                int qtde = res.getInt("qtde");
                int qtdeMin = res.getInt("qtdeMinima");
                int qtdeMax = res.getInt("qtdeMaxima");

                produtosList.add(new Produto(id, cat, marca, nome, preco, forn, qtde, qtdeMin, qtdeMax));
            }

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter registros no banco.");
        }

        tbl.setItems(produtosList);
    }

    private void atualizarTodasChoiceBox() {
        atualizarChoiceBoxCategorias();
        atualizarChoiceBoxMarcas();
        atualizarChoiceBoxFornecedores();
    }

    private void atualizarChoiceBoxCategorias() {
        ObservableList<String> categoriasList = FXCollections.observableArrayList();
        categoriasList.add(0, CATEGORIA_DEFAULT);

        try (ResultSet res = DatabaseManager.executarConsulta(SQL_SELECT_CATEGORIA)) {
            while (res.next()) {
                String nome = res.getString("nome");
                categoriasList.add(nome);
            }

            Platform.runLater(() -> {
                boxCategoria.setItems(categoriasList);
                boxCategoria.setValue(CATEGORIA_DEFAULT);
            });

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter registros de categorias no banco.");
        }
    }

    private void atualizarChoiceBoxMarcas() {
        ObservableList<String> marcasList = FXCollections.observableArrayList();
        marcasList.add(0, MARCA_DEFAULT);

        try (ResultSet res = DatabaseManager.executarConsulta(SQL_SELECT_MARCA)) {
            while (res.next()) {
                String nome = res.getString("nome");
                marcasList.add(nome);
            }

            Platform.runLater(() -> {
                boxMarca.setItems(marcasList);
                boxMarca.setValue(MARCA_DEFAULT);
            });

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter resgistros de marcas no banco.");
        }
    }

    private void atualizarChoiceBoxFornecedores() {
        ObservableList<String> fornList = FXCollections.observableArrayList();
        fornList.add(0, FORNECEDOR_DEFAULT);

        try (ResultSet res = DatabaseManager.executarConsulta(SQL_SELECT_FORNECEDOR)) {
            while (res.next()) {
                String nome = res.getString("nomeFantasia");
                fornList.add(nome);
            }

            Platform.runLater(() -> {
                boxFornecedor.setItems(fornList);
                boxFornecedor.setValue(FORNECEDOR_DEFAULT);
            });

            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao obter resgistros de fornecedores no banco.");
        }
    }

    @Override
    protected void restaurarValoresVariaveis() {
        boxFornecedor.setValue(FORNECEDOR_DEFAULT);
        boxCategoria.setValue(CATEGORIA_DEFAULT);
        boxMarca.setValue(MARCA_DEFAULT);
        txtNome.setText(null);
        txtPreco.setText(null);
        txtQtde.setText(null);
        txtQtdeMinima.setText(null);
        txtQtdeMaxima.setText(null);
        txtId.setText(null);
    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTodasChoiceBox();
        atualizarTabela();
    }

    private void configurarColunasTableView() {
        colIdProduto.setCellValueFactory(new PropertyValueFactory<>("idProduto"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colQtde.setCellValueFactory(new PropertyValueFactory<>("qtde"));
        colQtdeMinima.setCellValueFactory(new PropertyValueFactory<>("qtdeMinima"));
        colQtdeMaxima.setCellValueFactory(new PropertyValueFactory<>("qtdeMaxima"));
    }

    private void ajustarLarguraColunas() {
        colIdProduto.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colCategoria.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colMarca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colNome.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colPreco.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colFornecedor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colQtde.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colQtdeMinima.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
        colQtdeMaxima.prefWidthProperty().bind(tbl.widthProperty().multiply(0.11));
    }

}
