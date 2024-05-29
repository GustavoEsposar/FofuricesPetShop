package dev.gustavoesposar.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/petshop";
    private static final String USUARIO = "root";
    private static final String SENHA = "150909";

    private static Connection conexao;

    private DatabaseManager() {
    }

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conexao;
    }

    public static void fecharConexao() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    public static boolean executarUpdate(String sql, String... params) {
        try {
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            int res = statement.executeUpdate();
            DatabaseManager.fecharConexao();
            return res > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean executarUpdate(String sql, BigDecimal valor, String... params) {
        try {
            PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
            statement.setBigDecimal(1, valor);
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 2, params[i]);
            }
            int res = statement.executeUpdate();
            DatabaseManager.fecharConexao();
            return res > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ResultSet executarConsulta(String sql) throws SQLException {
        Connection conn = DatabaseManager.getConexao();
        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet res = statement.executeQuery();
        return res;
    }

    public static ResultSet executarConsulta(String sql, String... params) throws SQLException {
        Connection conn = DatabaseManager.getConexao();
        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        ResultSet res = statement.executeQuery();
        return res;
    }
}
