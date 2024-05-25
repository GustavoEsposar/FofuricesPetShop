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
    private final String sqlInsert = "INSERT INTO pet (Raca_idRaca, Fornecedor_idFornecedor, valor) " +
            "SELECT r.idRaca, f.idFornecedor, ? " +
            "FROM (SELECT idRaca FROM raca WHERE nome = ?) r, " +
            "(SELECT idFornecedor FROM fornecedor WHERE nomeFantasia = ?) f " +
            "WHERE NOT EXISTS (SELECT 1 FROM pet WHERE Raca_idRaca = r.idRaca AND Fornecedor_idFornecedor = f.idFornecedor)";
    private final String sqlSelect =
            "SELECT p.idPet, e.nome AS especieNome, r.nome AS racaNome, f.nomeFantasia AS fornecedorNome, p.valor " +
            "FROM pet p " +
            "JOIN raca r ON p.Raca_idRaca = r.idRaca " +
            "JOIN especie e ON r.Especie_idEspecie = e.idEspecie " +
            "JOIN fornecedor f ON p.Fornecedor_idFornecedor = f.idFornecedor";
    private final String sqlDelete = "DELETE FROM Pet WHERE idPet = ? LIMIT 1;";
    private final String sqlUpdatePet = 
            "UPDATE pet p " +
            "SET " +
            "   p.valor = ?, " +
            "   p.Raca_idRaca = ( " +
            "       SELECT idRaca " +
            "       FROM raca " +
            "       WHERE nome = ? " +
            "   ), " +
            "   p.Fornecedor_idFornecedor = ( " +
            "       SELECT idFornecedor " +
            "       FROM fornecedor " +
            "       WHERE nomeFantasia = ? " +
            "   ) " +
            "WHERE p.idPet = ?";

    @FXML
    private ChoiceBox<String> boxFornecedor;

    @FXML
    private ChoiceBox<String> boxRaca;

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
    private TableView<Pet> tbl;

    @FXML
    private TextField txtAdd;

    @FXML
    private TextField txtId;

    @FXML
    private void adicionar(ActionEvent event) {
        String raca = boxRaca.getValue().substring(boxRaca.getValue().indexOf(" ") + 1);
        String fornecedor = boxFornecedor.getValue();
        BigDecimal valor = new BigDecimal(txtAdd.getText());

        boolean sucesso;
        if (btnAdd.getText().equals("Update")) {
            btnAdd.setText("Adicionar");
            sucesso = DatabaseManager.executarUpdate(sqlUpdatePet, valor, raca, fornecedor, txtId.getText());
        } else {
            sucesso = DatabaseManager.executarUpdate(sqlInsert, valor, raca, fornecedor);
        }
        
        super.processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @FXML
    private void atualizar(ActionEvent event) {
        if (btnAdd.getText().equals("Update")) {
            return;
        }

        btnAdd.setText("Update");
        String sql = sqlSelect.replace("p.idPet,", "").concat(" WHERE idPet = " + txtId.getText());

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {
            if (resultSet.next()) {
                String especieNome = resultSet.getString("especieNome");
                String racaNome = resultSet.getString("racaNome");
                String fornecedorNome = resultSet.getString("fornecedorNome");
                String valor = resultSet.getString("valor");

                boxRaca.setValue(especieNome + " " + racaNome);
                boxFornecedor.setValue(fornecedorNome);
                txtAdd.setText(valor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void remover(ActionEvent event) {
        String idSelecionado = txtId.getText();

        boolean sucesso = DatabaseManager.executarUpdate(sqlDelete, idSelecionado);

        super.processarResultado(sucesso);
        restaurarValoresVariaveis();
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Pet> petsList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sqlSelect)) {

            while (resultSet.next()) {
                int idPet = resultSet.getInt("idPet");
                String especieNome = resultSet.getString("especieNome");
                String racaNome = resultSet.getString("racaNome");
                String fornecedorNome = resultSet.getString("fornecedorNome");
                BigDecimal valor = resultSet.getBigDecimal("valor");

                Pet pet = new Pet(idPet, especieNome, racaNome, fornecedorNome, valor);
                petsList.add(pet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        tbl.setItems(petsList);
    }

    private void atualizarTodasChoiceBox() {
        atualizarChoiceBoxRacas();
        atualizarChoiceBoxFornecedores();
    }

    private void atualizarChoiceBoxRacas() {
        ObservableList<String> racasList = FXCollections.observableArrayList();

        String sql = "SELECT E.nome AS nomeEspecie, R.nome AS nomeRaca " +
                "FROM Raca R " +
                "JOIN Especie E ON R.Especie_idEspecie = E.idEspecie";

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
            e.printStackTrace();
        }
    }

    private void atualizarChoiceBoxFornecedores() {
        ObservableList<String> fornecedoresList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta("SELECT nomeFantasia FROM fornecedor")) {
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
            e.printStackTrace();
        }
    }

    @Override
    protected void restaurarValoresVariaveis() {
        boxRaca.setValue("Espécie Raça");
        boxFornecedor.setValue("Fornecedor");
        txtAdd.setText(null);
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
        colIdPet.setCellValueFactory(new PropertyValueFactory<>("idPet"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especieNome"));
        colRaca.setCellValueFactory(new PropertyValueFactory<>("racaNome"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedorNome"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    private void ajustarLarguraColunas() {
        colIdPet.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colEspecie.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colRaca.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colValor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
        colFornecedor.prefWidthProperty().bind(tbl.widthProperty().multiply(0.2));
    }

}
