package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BazaPodataka {
    public static Connection uspostaviVezu() throws SQLException {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        if (url == null || username == null || password == null) {
            throw new SQLException("Postavite DB_URL, DB_USER i DB_PASSWORD u postavkama pokretanja.");
        }
        return DriverManager.getConnection(url, username, password);
    }
}
