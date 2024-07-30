package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.utils.EmissorDeRelatorio;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

public class RelatoriosController extends OpcaoDoMenu {
    private static final String OPCAO_DEFAULT = "Selecione um Relatório";
    private static final String SQL_SELECT_OPCOES = "select\n" + //
            "\tnome\n" + //
            "from relatorio;";
    private static final String SQL_SELECT_OBTER_QUERY = "select\n" + //
            "\tsqlRelatorio\n" + //
            "from relatorio\n" + //
            "where nome = ?;";

    @FXML
    private ChoiceBox<String> boxRelatorios;

    private void atualizarTabelaChoiceBox() {
        ObservableList<String> relatoriosList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(SQL_SELECT_OPCOES)) {
            relatoriosList.add(0, OPCAO_DEFAULT);
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                relatoriosList.add(nome);
            }

            Platform.runLater(() -> {
                boxRelatorios.setItems(relatoriosList);
                boxRelatorios.setValue(OPCAO_DEFAULT);
            });
            DatabaseManager.fecharConexao();
        } catch (SQLException e) {
            janelaDeErro("Erro ao consultar opções de relatórios.");
        }
    }

    @FXML
    private void emitirRelatorio() {

        try (ResultSet res = DatabaseManager.executarConsulta(SQL_SELECT_OBTER_QUERY, boxRelatorios.getValue())) {
            if (res.next()) {
                String sql = res.getString("sqlRelatorio");
                EmissorDeRelatorio.gerarPDF(EmissorDeRelatorio.selectFile(), sql);
            }
        } catch (Exception e) {
            janelaDeErro(e.toString());
        }

        restaurarValoresVariaveis();
    }

    @Override
    protected void restaurarValoresVariaveis() {
        boxRelatorios.setValue(OPCAO_DEFAULT);
    }

    @FXML
    public void initialize() {
        atualizarTabelaChoiceBox();
    }

    @Override
    protected void atualizarTabela() {
    }

    protected void janelaInformativa(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null); // Sem cabeçalho
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
