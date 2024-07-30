package dev.gustavoesposar.controller;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Pet;
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

public final class PetsController extends OpcaoDoMenu {
    private final String IDADE_DEFAULT = "Selecione a idade";
    private final String SQL_INSERT = 
    "INSERT INTO pet (Raca_idRaca, Fornecedor_idFornecedor, valor, idade, qtde)\n" + //
    "SELECT r.idRaca, f.idFornecedor, ?, ?, ?\n" + //
    "FROM (SELECT idRaca FROM raca WHERE nome = ?) r,\n" + //
    "     (SELECT idFornecedor FROM fornecedor WHERE nomeFantasia = ?) f\n" + //
    "WHERE NOT EXISTS (SELECT 1 FROM pet WHERE Raca_idRaca = r.idRaca AND Fornecedor_idFornecedor = f.idFornecedor AND idade = ?);";
    private final String SQL_SELECT_PET = "SELECT p.idPet, e.nome AS especieNome, r.nome AS racaNome, f.nomeFantasia AS fornecedorNome, p.valor, p.qtde , p.idade "
            +
            "FROM pet p " +
            "JOIN raca r ON p.Raca_idRaca = r.idRaca " +
            "JOIN especie e ON r.Especie_idEspecie = e.idEspecie " +
            "JOIN fornecedor f ON p.Fornecedor_idFornecedor = f.idFornecedor";
    private final String SQL_DELETE = "DELETE FROM Pet WHERE idPet = ? LIMIT 1;";
    private final String SQL_UPDATE_PET = 
    "UPDATE pet p \n" + //
    "SET \n" + //
    "    p.valor = ?, \n" + //
    "    p.Raca_idRaca = ( \n" + //
    "        SELECT idRaca \n" + //
    "        FROM raca \n" + //
    "        WHERE nome = ? \n" + //
    "    ), \n" + //
    "    p.Fornecedor_idFornecedor = ( \n" + //
    "        SELECT idFornecedor \n" + //
    "        FROM fornecedor \n" + //
    "        WHERE nomeFantasia = ? \n" + //
    "    ),\n" + //
    "    p.idade = ?,\n" + //
    "    p.qtde = ?\n" + //
    "WHERE p.idPet = ?\n" + //
    "";

    @FXML
    private ChoiceBox<String> boxFornecedor;

    @FXML
    private ChoiceBox<String> boxRaca;

    @FXML
    private ChoiceBox<String> boxIdade;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Pet, Integer> colIdPet;

    @FXML
    private TableColumn<Pet, String> colEspecie;

    @FXML
    private TableColumn<Pet, String> colRaca;

    @FXML
    private TableColumn<Pet, String> colFornecedor;

    @FXML
    private TableColumn<Pet, BigDecimal> colValor;

    @FXML
    private TableColumn<Pet, Integer> colQtde;

    @FXML
    private TableColumn<Pet, String> colIdade;

    @FXML
    private TableView<Pet> tbl;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtQtde;

    @FXML
    private TextField txtId;

    @FXML
    private void adicionar(ActionEvent event) {
        try {
            String raca = boxRaca.getValue().substring(boxRaca.getValue().indexOf(" ") + 1);
            String fornecedor = boxFornecedor.getValue();
            BigDecimal valor = new BigDecimal(txtAdd.getText());
            String idade = boxIdade.getValue();
            String qtde = txtQtde.getText();

            if (btnAdd.getText().equals("Update")) {
                btnAdd.setText("Adicionar");
                DatabaseManager.executarUpdate(SQL_UPDATE_PET, valor, raca, fornecedor, idade, qtde, txtId.getText());
            } else {
                DatabaseManager.executarUpdate(SQL_INSERT, valor, idade, qtde, raca, fornecedor, idade);
            }
            atualizarTabela();
            restaurarValoresVariaveis();
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos corretamente");
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
        String sql = SQL_SELECT_PET.replace("p.idPet,", "").concat(" WHERE idPet = " + txtId.getText());

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {
            if (resultSet.next()) {
                String especieNome = resultSet.getString("especieNome");
                String racaNome = resultSet.getString("racaNome");
                String fornecedorNome = resultSet.getString("fornecedorNome");
                String valor = resultSet.getString("valor");
                String idade = resultSet.getString("idade");
                String qtde = resultSet.getString("qtde");

                boxRaca.setValue(especieNome + " " + racaNome);
                boxFornecedor.setValue(fornecedorNome);
                boxIdade.setValue(idade);
                txtAdd.setText(valor);
                txtQtde.setText(qtde);
            }

        } catch (SQLException e) {
            janelaDeErro("Erro de comunicação com o banco de dados");
        }
    }

    @FXML
    private void remover(ActionEvent event) {

        try {
            String idSelecionado = txtId.getText();
            DatabaseManager.executarUpdate(SQL_DELETE, idSelecionado);
            atualizarTabela();
            restaurarValoresVariaveis();
        } catch (Exception e) {
            janelaDeErro(e.toString());
        }
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Pet> petsList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(SQL_SELECT_PET)) {

            while (resultSet.next()) {
                int idPet = resultSet.getInt("idPet");
                String especieNome = resultSet.getString("especieNome");
                String racaNome = resultSet.getString("racaNome");
                String fornecedorNome = resultSet.getString("fornecedorNome");
                BigDecimal valor = resultSet.getBigDecimal("valor");
                String idade = resultSet.getString("idade");
                int qtde = resultSet.getInt("qtde");

                Pet pet = new Pet(idPet, especieNome, racaNome, fornecedorNome, valor, idade, qtde);
                petsList.add(pet);
            }

        } catch (SQLException e) {
            janelaDeErro(e.toString());
        }

        tbl.setItems(petsList);
    }

    private void atualizarTodasChoiceBox() {
        atualizarChoiceBoxRacas();
        atualizarChoiceBoxFornecedores();
        atualizarChoiceBoxIdade();
    }

    private void atualizarChoiceBoxRacas() {
        ObservableList<String> racasList = FXCollections.observableArrayList();

        String sql = "SELECT E.nome AS nomeEspecie, R.nome AS nomeRaca " +
                "FROM Raca R " +
                "JOIN Especie E ON R.Especie_idEspecie = E.idEspecie ORDER BY E.nome, R.nome";

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {
            racasList.add(0, "Espécie Raça");
            while (resultSet.next()) {
                String nomeEspecie = resultSet.getString("nomeEspecie");
                String nomeRaca = resultSet.getString("nomeRaca");
                String nomeCompleto = nomeEspecie + " " + nomeRaca;
                racasList.add(nomeCompleto);
            }

            Platform.runLater(() -> {
                boxRaca.setItems(racasList);
                boxRaca.setValue("Espécie Raça");
            });
            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao consultar opções de raça");
        }
    }

    private void atualizarChoiceBoxFornecedores() {
        ObservableList<String> fornecedoresList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager
                .executarConsulta("SELECT nomeFantasia FROM fornecedor ORDER BY nomeFantasia")) {
            fornecedoresList.add(0, "Fornecedor");
            while (resultSet.next()) {
                String nomeFantasia = resultSet.getString("nomeFantasia");
                fornecedoresList.add(nomeFantasia);
            }

            Platform.runLater(() -> {
                boxFornecedor.setItems(fornecedoresList);
                boxFornecedor.setValue("Fornecedor");
            });
            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao consultar opções de fornecedores");
        }
    }

    private void atualizarChoiceBoxIdade() {
        ObservableList<String> idadesList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(
                "SELECT COLUMN_TYPE " +
                        "FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE TABLE_NAME = 'PET' " +
                        "AND COLUMN_NAME = 'idade'")) {

            if (resultSet.next()) {
                String columnType = resultSet.getString("COLUMN_TYPE");

                // Extraindo os valores do ENUM
                String enumValues = columnType.substring(columnType.indexOf("(") + 1, columnType.indexOf(")"));
                String[] portes = enumValues.split(",");

                idadesList.add(0, IDADE_DEFAULT);
                for (String porte : portes) {
                    porte = porte.replace("'", "").trim();
                    idadesList.add(porte);
                }

                Platform.runLater(() -> {
                    boxIdade.setItems(idadesList);
                    boxIdade.setValue(IDADE_DEFAULT);
                });
            }
        } catch (SQLException e) {
            janelaDeErro("Erro ao consultar opções de porte");
        }
    }

    @Override
    protected void restaurarValoresVariaveis() {
        boxRaca.setValue("Espécie Raça");
        boxFornecedor.setValue("Fornecedor");
        txtAdd.setText(null);
        txtId.setText(null);
        boxIdade.setValue(IDADE_DEFAULT);
        txtQtde.setText(null);
    }

    @FXML
    public void initialize() {
        configurarColunasTableView();
        ajustarLarguraColunas();
        atualizarTodasChoiceBox();
        atualizarTabela();
    }

    private void configurarColunasTableView() {
        colIdPet.setCellValueFactory(new PropertyValueFactory<>("idPet"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especieNome"));
        colRaca.setCellValueFactory(new PropertyValueFactory<>("racaNome"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedorNome"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colQtde.setCellValueFactory(new PropertyValueFactory<>("qtde"));
    }

    private void ajustarLarguraColunas() {
        colIdPet.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colEspecie.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colRaca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colValor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colFornecedor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colIdade.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
        colQtde.prefWidthProperty().bind(tbl.widthProperty().multiply(0.14));
    }

}
