package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class PovijestRezervacijaModel {

    public static List<String[]> getPovijestRezervacija(int idKorisnika) throws SQLException {
        List<String[]> rezervacije = new ArrayList<>();

        String query =
                "SELECT r.rezervacija_id,\n" +
                        "    l.broj_leta,\n" +
                        "    l.grad_polaska,\n" +
                        "    l.grad_dolaska,\n" +
                        "    l.vrijeme_polaska,\n" +
                        "    r.broj_sjedista,\n" +
                        "    a.model_aviona,\n" +
                        "    r.datum_rezervacije\n" +
                        "FROM REZERVACIJE_LETOVA r\n" +
                        "JOIN LETOVI l ON r.let_id = l.let_id\n" +
                        "JOIN AVION a ON l.id_avion = a.id_aviona\n" +
                        "WHERE r.korisnik_id = ?\n" +
                        "ORDER BY r.datum_rezervacije DESC;";

        try (Connection conn = uspostaviVezu();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idKorisnika);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rezervacije.add(new String[]{
                        String.valueOf(rs.getInt("rezervacija_id")),
                        rs.getString("broj_leta"),
                        rs.getString("grad_polaska"),
                        rs.getString("grad_dolaska"),
                        String.valueOf(rs.getTimestamp("vrijeme_polaska")),
                        String.valueOf(rs.getInt("broj_sjedista")),
                        rs.getString("model_aviona"),
                        String.valueOf(rs.getTimestamp("datum_rezervacije"))
                });
            }
        } catch (SQLException e) {
            throw new SQLException("Greška pri dohvaćanju podataka o rezervacijama.", e);
        }

        return rezervacije;
    }

}
