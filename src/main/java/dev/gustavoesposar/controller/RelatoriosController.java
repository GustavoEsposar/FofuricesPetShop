package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class RelatoriosController extends OpcaoDoMenu {
    private static final String OPCAO_DEFAULT = "Selecione um Relatório";
    private static final String SQL_SELECT = 
    "select\n" + //
    "\tnome\n" + //
    "from relatorio;";

    @FXML
    private ChoiceBox<String> boxRelatorios;

    private void atualizarTabelaChoiceBox() {
        ObservableList<String> relatoriosList = FXCollections.observableArrayList();

        try (ResultSet resultSet = DatabaseManager.executarConsulta(SQL_SELECT)) {
            relatoriosList.add(0, OPCAO_DEFAULT);
            while (resultSet.next()) {
                String nome = resultSet.getString("");
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

    @Override
    protected void restaurarValoresVariaveis() {

    }

    @FXML
    public void initialize() {
        atualizarTabelaChoiceBox();
    }

    @Override
    protected void atualizarTabela() {
    }
}
