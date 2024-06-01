package dev.gustavoesposar.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import dev.gustavoesposar.controller.abstracts.OpcaoDoMenu;
import dev.gustavoesposar.database.DatabaseManager;
import dev.gustavoesposar.model.Login;
import dev.gustavoesposar.utils.AutenticacaoEmail;
import dev.gustavoesposar.utils.AutenticacaoSenha;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class LoginsController extends OpcaoDoMenu{
    private final String sqlDelete = "DELETE FROM Login WHERE idLogin = ? LIMIT 1;";
    private final String sqlSelect = "SELECT idLogin, email FROM login;";
    private final String sqlInsert =    "INSERT INTO login (email, senha)\r\n" + //
                                        "SELECT ?, ? FROM DUAL\r\n" + //
                                        "WHERE NOT EXISTS (\r\n" + //
                                        "    SELECT 1 FROM login WHERE email = ?\r\n" + //
                                        ")\r\n" + //
                                        "";
    private final String sqlUpdate =    "UPDATE login\n" + //
                                        "SET \n" + //
                                        "\temail = ?,\n" + //
                                        "\tsenha = ?\n" + //
                                        "WHERE idLogin = ?\n" + //
                                        "";

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRm;

    @FXML
    private Button btnUp;

    @FXML
    private TableColumn<Login, String> colEmail;

    @FXML
    private TableColumn<Login, Integer> colIdLogin;

    @FXML
    private TableView<Login> tbl;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private PasswordField txtSenhaConfirmar;

    @FXML
    private void adicionar(ActionEvent event) {        
        try {   
            if (senhasForamPreenchidas(txtSenha.getText(), txtSenhaConfirmar.getText())) {
                String email = AutenticacaoEmail.verificarEmailCorreto(txtEmail.getText());
                String senha = txtSenha.getText();
                if (btnAdd.getText().equals("Update")) {
                    btnAdd.setText("Adicionar");
                    DatabaseManager.executarUpdate(sqlUpdate, email, AutenticacaoSenha.gerarHash(senha), txtId.getText());
                } else {
                    DatabaseManager.executarUpdate(sqlInsert, email, AutenticacaoSenha.gerarHash(senha), email);
                    atualizarTabela();
                    restaurarValoresVariaveis();
                }
            }
        } catch (NullPointerException | NumberFormatException e) {
            janelaDeErro("Preencha os campos corretamente");
        } catch (Exception e) {
            janelaDeErro(e.toString());
        }
    }

    @FXML
    private void atualizar(ActionEvent event) {
        if (btnAdd.getText().equals("Update")) {
            return;
        }

        String sql = "select email, senha from login where idLogin = ? limit 1";
        
        try(ResultSet res = DatabaseManager.executarConsulta(sql, txtId.getText())) {
            if (res.next()) {
                txtEmail.setText(res.getString("email"));
                txtSenha.setText(null);
                txtSenhaConfirmar.setText(null);
                btnAdd.setText("Update");
            } else {
                throw new IllegalArgumentException("Verifique o ID informado!");
            }
        } catch(Exception e) {
            janelaDeErro(e.toString());
        }
    }

    @FXML
    private void remover(ActionEvent event) {
        try {
            String id = txtId.getText();
            DatabaseManager.executarUpdate(sqlDelete, id);
            atualizarTabela();
        } catch (Exception e) {
            janelaDeErro(e.toString());
        } finally {
            restaurarValoresVariaveis();
        }
    }

    @Override
    protected void atualizarTabela() {
        ObservableList<Login> loginsList = FXCollections.observableArrayList();

        try(ResultSet res = DatabaseManager.executarConsulta(sqlSelect)) {
            while (res.next()) {
                int id = res.getInt("idLogin");
                String email = res.getString("email");

                Login login = new Login(id, email);
                loginsList.add(login);
            }
        } catch(SQLException e) {
            janelaDeErro("Erro ao obter registros no banco");
        }

        tbl.setItems(loginsList);
    }

    @Override
    protected void restaurarValoresVariaveis() {
        txtEmail.setText(null);
        txtSenha.setText(null);
        txtId.setText(null);
        txtSenhaConfirmar.setText(null);
    }

    private boolean senhasForamPreenchidas(String senha, String senhaConfirmar) {
        saoSenhasNaoNulas(senha, senhaConfirmar);
        saoSenhasNaoVazias(senha, senhaConfirmar);
        saoSenhasIguais(senha, senhaConfirmar);
        AutenticacaoSenha.ehSenhaSegura(senha);
        return true;
    } 

    private void saoSenhasNaoNulas(String senha, String senhaConfirmar) {
        if (senha == null || senhaConfirmar == null) {
            throw new NullPointerException("Uma ou ambas as senhas são nulas.");
        }
    }

    private void saoSenhasNaoVazias(String senha, String senhaConfirmar) {
        if (senha.isEmpty() || senhaConfirmar.isEmpty()) {
            throw new IllegalArgumentException("Uma ou ambas as senhas estão vazias.");
        }
    }

    private void saoSenhasIguais(String senha, String senhaConfirmar) {
        if (!senha.equals(senhaConfirmar)) {
            throw new IllegalArgumentException("As senhas não são iguais.");
        }
    }    

    @FXML
    public void initialize() {
        configurarColunasTableView(); 
        ajustarLarguraColunas();
        atualizarTabela();
    }
    
    private void configurarColunasTableView() {
        colIdLogin.setCellValueFactory(new PropertyValueFactory<>("idLogin"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    
    private void ajustarLarguraColunas() {
        colIdLogin.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
        colEmail.prefWidthProperty().bind(tbl.widthProperty().multiply(0.5));
    }

}
