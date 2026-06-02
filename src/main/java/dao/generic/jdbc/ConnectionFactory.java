package dao.generic.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/vendasdb";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Driver do PostgreSQL não encontrado", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
