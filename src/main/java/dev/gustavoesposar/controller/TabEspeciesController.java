package dev.gustavoesposar.controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dev.gustavoesposar.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class TabEspeciesController extends PrincipalController {
    

    @FXML   
    public void adicionarEspecie() {
        //adiciona apenas se não existir registro
        String nomeEspecie = super.txtAddEspecie.getText();
        
        try {
            String sql = "INSERT INTO Especie (nome) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM Especie WHERE nome = ?);\n";
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            statement.setString(1, nomeEspecie);
            statement.setString(2, nomeEspecie);

            ResultSet res = statement.executeQuery();

            if (res.next()) {
                atualizarTabela();
            } else {
                janelaErroDeInsercao();
            }

        } catch (Exception e) {
            super.janelaErroDeConexao();
        }
    }

    @FXML
    public void removerEspecie() {

    }

    private void atualizarTabela() {

    }

    private void janelaErroDeInsercao() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de inserção");
        alert.setHeaderText("Falha ao inserir no banco de dados");
        alert.showAndWait();
    }
}
