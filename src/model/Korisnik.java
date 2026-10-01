package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class Korisnik {
    private int id;
    private String ime;
    private String email;
    private String lozinka;
    private Timestamp datumKreiranja;
    private String uloga;
    private String kontaktBrojKorisnika;

    // Konstruktori
    public Korisnik() {
    }

    public Korisnik(int id, String ime, String email, String lozinka, Timestamp datumKreiranja, String uloga, String kontaktBrojKorisnika) {
        this.id = id;
        this.ime = ime;
        this.email = email;
        this.lozinka = lozinka;
        this.datumKreiranja = datumKreiranja;
        this.uloga = uloga;
        this.kontaktBrojKorisnika = kontaktBrojKorisnika;
    }

    // Prijava korisnika (uloga = 'user')
    public static Korisnik prijaviKorisnika(String email, String lozinka) {
        String hesiranaLozinka = SigurnostUtil.hesirajLozinku(lozinka);
        String upit = "SELECT id, ime, email, lozinka, kreirano, uloga, kontaktBroj_korisnika " +
                "FROM KORISNIK WHERE email = ? AND lozinka = ? AND uloga = 'user'";

        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement(upit)) {

            stmt.setString(1, email);
            stmt.setString(2, hesiranaLozinka);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Korisnik(
                        rs.getInt("id"),
                        rs.getString("ime"),
                        rs.getString("email"),
                        rs.getString("lozinka"),
                        rs.getTimestamp("kreirano"),
                        rs.getString("uloga"),
                        rs.getString("kontaktBroj_korisnika")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Prijava administratora direktno iz tablice Korisnik
    public static Korisnik prijaviAdministratoraIzKorisnika(String email, String lozinka) {
        String hesiranaLozinka = SigurnostUtil.hesirajLozinku(lozinka);
        String upit = "SELECT id, ime, email, lozinka, kreirano, uloga, kontaktBroj_korisnika " +
                "FROM KORISNIK WHERE email = ? AND lozinka = ? AND uloga = 'admin'";

        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement(upit)) {

            stmt.setString(1, email);
            stmt.setString(2, hesiranaLozinka);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Korisnik(
                        rs.getInt("id"),
                        rs.getString("ime"),
                        rs.getString("email"),
                        rs.getString("lozinka"),
                        rs.getTimestamp("kreirano"),
                        rs.getString("uloga"),
                        rs.getString("kontaktBroj_korisnika")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean dodajNovogKorisnika(String ime, String email, String lozinka, String kontaktBroj, String uloga) {
        String upit = "INSERT INTO KORISNIK (ime, email, lozinka, uloga, kontaktBroj_korisnika) VALUES (?, ?, ?, ?, ?)";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {

            izjava.setString(1, ime.trim());
            izjava.setString(2, email.trim());
            izjava.setString(3, SigurnostUtil.hesirajLozinku(lozinka.trim()));
            izjava.setString(4, (uloga != null && !uloga.trim().isEmpty()) ? uloga.trim() : "user");
            izjava.setString(5, kontaktBroj.trim());

            return izjava.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean azurirajKorisnika(int id, String ime, String email, String lozinka, String kontaktBroj) {
        boolean mijenjaLozinku = lozinka != null && !lozinka.trim().isEmpty();
        String upit = mijenjaLozinku
                ? "UPDATE KORISNIK SET ime=?, email=?, lozinka=?, kontaktBroj_korisnika=? WHERE id=?"
                : "UPDATE KORISNIK SET ime=?, email=?, kontaktBroj_korisnika=? WHERE id=?";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {

            int indeks = 1;
            izjava.setString(indeks++, ime.trim());
            izjava.setString(indeks++, email.trim());
            if (mijenjaLozinku) {
                izjava.setString(indeks++, SigurnostUtil.hesirajLozinku(lozinka.trim()));
            }
            izjava.setString(indeks++, kontaktBroj.trim());
            izjava.setInt(indeks, id);

            return izjava.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void hesirajPostojeceLozinke() {
        String upitOdabir = "SELECT id, lozinka FROM KORISNIK";
        String upitAzuriranje = "UPDATE KORISNIK SET lozinka = ? WHERE id = ?";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjavaOdabir = veza.prepareStatement(upitOdabir);
             ResultSet rs = izjavaOdabir.executeQuery();
             PreparedStatement izjavaAzuriranje = veza.prepareStatement(upitAzuriranje)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String lozinka = rs.getString("lozinka");
                if (lozinka != null && lozinka.length() != 64) {
                    izjavaAzuriranje.setString(1, SigurnostUtil.hesirajLozinku(lozinka));
                    izjavaAzuriranje.setInt(2, id);
                    izjavaAzuriranje.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Korisnik> ucitajSveKorisnike() {
        List<Korisnik> korisnici = new ArrayList<>();
        String upit = "SELECT id, ime, email, kreirano, uloga, kontaktBroj_korisnika FROM KORISNIK WHERE uloga = 'user' ORDER BY ime";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit);
             ResultSet rezultati = izjava.executeQuery()) {

            while (rezultati.next()) {
                korisnici.add(new Korisnik(
                        rezultati.getInt("id"),
                        rezultati.getString("ime"),
                        rezultati.getString("email"),
                        "",
                        rezultati.getTimestamp("kreirano"),
                        rezultati.getString("uloga"),
                        rezultati.getString("kontaktBroj_korisnika")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return korisnici;
    }

    // Getteri i Setteri
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    public Timestamp getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(Timestamp datumKreiranja) { this.datumKreiranja = datumKreiranja; }

    public String getUloga() { return uloga; }
    public void setUloga(String uloga) { this.uloga = uloga; }

    public String getKontaktBrojKorisnika() { return kontaktBrojKorisnika; }
    public void setKontaktBrojKorisnika(String kontaktBrojKorisnika) { this.kontaktBrojKorisnika = kontaktBrojKorisnika; }

//    @Override
//    public String toString() {
//        return "Korisnik{" + "id=" + id + ", ime='" + ime + '\'' + ", email='" + email + '\'' + ", uloga='" + uloga + '\'' + '}';
//    }

    @Override
    public String toString() {
        return ime;
    }
}