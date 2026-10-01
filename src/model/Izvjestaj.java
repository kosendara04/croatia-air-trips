package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class Izvjestaj {
    private int idIzvjestaja;
    private int idKorisnika;
    private String gradPolaska;
    private String gradDolaska;
    private Timestamp vrijemePolaska;
    private String korisnikIme;

    public Izvjestaj() {
    }

    public Izvjestaj(int idKorisnika, String gradPolaska, String gradDolaska, Timestamp vrijemePolaska) {
        this.idIzvjestaja = -1;
        this.idKorisnika = idKorisnika;
        this.gradPolaska = gradPolaska;
        this.gradDolaska = gradDolaska;
        this.vrijemePolaska = vrijemePolaska;
    }

    public Izvjestaj(int idKorisnika, String gradPolaska, String gradDolaska, Timestamp vrijemePolaska, String korisnikIme) {
        this.idIzvjestaja = -1;
        this.idKorisnika = idKorisnika;
        this.gradPolaska = gradPolaska;
        this.gradDolaska = gradDolaska;
        this.vrijemePolaska = vrijemePolaska;
        this.korisnikIme = korisnikIme;
    }

    public int getIdIzvjestaja() {
        return idIzvjestaja;
    }

    public void setIdIzvjestaja(int idIzvjestaja) {
        this.idIzvjestaja = idIzvjestaja;
    }

    public int getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(int idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public String getGradPolaska() {
        return gradPolaska;
    }

    public void setGradPolaska(String gradPolaska) {
        this.gradPolaska = gradPolaska;
    }

    public String getGradDolaska() {
        return gradDolaska;
    }

    public void setGradDolaska(String gradDolaska) {
        this.gradDolaska = gradDolaska;
    }

    public Timestamp getVrijemePolaska() {
        return vrijemePolaska;
    }

    public void setVrijemePolaska(Timestamp vrijemePolaska) {
        this.vrijemePolaska = vrijemePolaska;
    }

    public String getKorisnikIme() {
        return korisnikIme;
    }

    public void setKorisnikIme(String korisnikIme) {
        this.korisnikIme = korisnikIme;
    }

    @Override
    public String toString() {
        return "Izvjestaj{" +
                "idIzvjestaja=" + idIzvjestaja +
                ", idKorisnika=" + idKorisnika +
                ", gradPolaska='" + gradPolaska + '\'' +
                ", gradDolaska='" + gradDolaska + '\'' +
                ", vrijemePolaska=" + vrijemePolaska +
                ", korisnikIme='" + korisnikIme + '\'' +
                '}';
    }

    public static List<Izvjestaj> getAllIzvjestaj() throws SQLException {
        List<Izvjestaj> izvjestaji = new ArrayList<>();
        String query = "SELECT r.izvjestaj_id, r.korisnik_id, r.grad_polaska, r.grad_dolaska, r.vrijeme_polaska, u.ime " +
                "FROM IZVJESTAJI r " +
                "JOIN KORISNIK u ON r.korisnik_id = u.id";

        try (PreparedStatement stmt = uspostaviVezu().prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Izvjestaj izvjestaj = new Izvjestaj(
                        rs.getInt("korisnik_id"),
                        rs.getString("grad_polaska"),
                        rs.getString("grad_dolaska"),
                        rs.getTimestamp("vrijeme_polaska"),
                        rs.getString("ime")
                );
                izvjestaji.add(izvjestaj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Greška u stvaranju izvjestaja.", e);
        }
        return izvjestaji;
    }

    public static boolean insertIzvjestaj(Izvjestaj izvjestaj) throws SQLException {
        String query = "INSERT INTO IZVJESTAJI (korisnik_id, grad_polaska, grad_dolaska, vrijeme_polaska) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = uspostaviVezu().prepareStatement(query)) {
            stmt.setInt(1, izvjestaj.getIdKorisnika());
            stmt.setString(2, izvjestaj.getGradPolaska());
            stmt.setString(3, izvjestaj.getGradDolaska());
            stmt.setTimestamp(4, izvjestaj.getVrijemePolaska());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error inserting report.", e);
        }
    }
}
