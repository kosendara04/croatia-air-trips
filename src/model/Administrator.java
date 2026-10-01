package model;

import java.sql.*;
import static model.BazaPodataka.uspostaviVezu;

public class Administrator {
    private int id;
    private String korisnickoIme;
    private String lozinka;
    private String ime;
    private Timestamp datumKreiranja;

    public Administrator() {
    }

    public Administrator(int id, String korisnickoIme, String lozinka, String ime, Timestamp datumKreiranja) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.ime = ime;
        this.datumKreiranja = datumKreiranja;
    }

    public static Administrator prijaviAdministratora(String korisnickoIme, String lozinka) {
        String hesiranaLozinka = SigurnostUtil.hesirajLozinku(lozinka);
        String upit = "SELECT id, ime, email, lozinka, kreirano FROM KORISNIK WHERE (email = ? OR ime = ?) AND lozinka = ? AND uloga = 'admin'";

        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement(upit)) {

            stmt.setString(1, korisnickoIme);
            stmt.setString(2, korisnickoIme);
            stmt.setString(3, hesiranaLozinka);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Administrator(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("lozinka"),
                        rs.getString("ime"),
                        rs.getTimestamp("kreirano")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Getteri i Setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getKorisnickoIme() { return korisnickoIme; }
    public void setKorisnickoIme(String korisnickoIme) { this.korisnickoIme = korisnickoIme; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public Timestamp getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(Timestamp datumKreiranja) { this.datumKreiranja = datumKreiranja; }
}