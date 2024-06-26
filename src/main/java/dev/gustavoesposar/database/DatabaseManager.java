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

    public static boolean executarUpdate(String sql, String... params) throws SQLException {
        boolean sucesso = false;

        PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        int res = statement.executeUpdate();
        DatabaseManager.fecharConexao();
        if (res > 0)
            sucesso = true;
        return sucesso;
    }

    public static boolean executarUpdate(String sql, BigDecimal valor, String... params) throws SQLException {
        boolean sucesso = false;

        PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql);
        statement.setBigDecimal(1, valor);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 2, params[i]);
        }
        int res = statement.executeUpdate();
        DatabaseManager.fecharConexao();

        if (res > 0)
            sucesso = true;
        return sucesso;
    }

    public static int executarUpdateLastId(String sql, BigDecimal valor, String... params) throws SQLException {
        PreparedStatement statement = DatabaseManager.getConexao().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setBigDecimal(1, valor);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 2, params[i]);
        }
        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        int lastProductId = 0;
        if (rs.next()) {
            lastProductId = rs.getInt(1);
        }

        DatabaseManager.fecharConexao();
        return lastProductId;
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

    public static void iniciarTransacao() throws SQLException {
        Connection conn = getConexao();
        conn.setAutoCommit(false);
    }

    public static void confirmarTransacao() throws SQLException {
        Connection conn = getConexao();
        conn.commit();
        conn.setAutoCommit(true);
        fecharConexao();
    }

    public static void reverterTransacao() throws SQLException {
        Connection conn = getConexao();
        conn.rollback();
        conn.setAutoCommit(true);
        fecharConexao();
    }

    public static boolean executarUpdateTransacao(Connection conn, String sql, String... params) throws SQLException {
        boolean sucesso = false;

        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 1, params[i]);
        }
        int res = statement.executeUpdate();
        if (res > 0)
            sucesso = true;
        return sucesso;
    }

    public static boolean executarUpdateTransacao(Connection conn, String sql, BigDecimal valor, String... params) throws SQLException {
        boolean sucesso = false;

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setBigDecimal(1, valor);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 2, params[i]);
        }
        int res = statement.executeUpdate();

        if (res > 0)
            sucesso = true;
        return sucesso;
    }

    public static int executarUpdateLastIdTransacao(Connection conn, String sql, BigDecimal valor, String... params) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setBigDecimal(1, valor);
        for (int i = 0; i < params.length; i++) {
            statement.setString(i + 2, params[i]);
        }
        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        int lastProductId = 0;
        if (rs.next()) {
            lastProductId = rs.getInt(1);
        }
        return lastProductId;
    }
}
