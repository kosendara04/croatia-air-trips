package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class Avion {
    private int idAviona;
    private int kapacitetAviona;
    private String modelAviona;
    private String proizvodacAviona;

    // Konstruktori
    public Avion() {
    }

    public Avion(int idAviona, int kapacitetAviona, String modelAviona, String proizvodacAviona) {
        this.idAviona = idAviona;
        this.kapacitetAviona = kapacitetAviona;
        this.modelAviona = modelAviona;
        this.proizvodacAviona = proizvodacAviona;
    }

    //  Dodavanje aviona
    public static boolean dodajAvion(int kapacitet, String model, String proizvodac) {
        String upit = "INSERT INTO AVION (kapacitet_aviona, model_aviona, proizvodac_aviona) VALUES (?, ?, ?)";
        
        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {
            
            izjava.setInt(1, kapacitet);
            izjava.setString(2, model.trim());
            izjava.setString(3, proizvodac.trim());
            
            return izjava.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  Ažuriranje aviona
    public static boolean azurirajAvion(int id, int kapacitet, String model, String proizvodac) {
        String upit = "UPDATE AVION SET kapacitet_aviona=?, model_aviona=?, proizvodac_aviona=? WHERE id_aviona=?";
        
        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {
            
            izjava.setInt(1, kapacitet);
            izjava.setString(2, model.trim());
            izjava.setString(3, proizvodac.trim());
            izjava.setInt(4, id);
            
            return izjava.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Brisanje aviona
    public static boolean obrisiAvion(int id) {
        String upit = "DELETE FROM AVION WHERE id_aviona=?";
        
        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {
            
            izjava.setInt(1, id);
            
            return izjava.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Dohvaćanje svih aviona
    public static List<Avion> ucitajSveAvione() {
        List<Avion> avioni = new ArrayList<>();
        String upit = "SELECT * FROM AVION ORDER BY proizvodac_aviona, model_aviona";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit);
             ResultSet rezultati = izjava.executeQuery()) {

            while (rezultati.next()) {
                avioni.add(new Avion(
                        rezultati.getInt("id_aviona"),
                        rezultati.getInt("kapacitet_aviona"),
                        rezultati.getString("model_aviona"),
                        rezultati.getString("proizvodac_aviona")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avioni;
    }

    // Getteri i Setteri
    public int getIdAviona() {
        return idAviona;
    }

    public void setIdAviona(int idAviona) {
        this.idAviona = idAviona;
    }

    public int getKapacitetAviona() {
        return kapacitetAviona;
    }

    public void setKapacitetAviona(int kapacitetAviona) {
        this.kapacitetAviona = kapacitetAviona;
    }

    public String getModelAviona() {
        return modelAviona;
    }

    public void setModelAviona(String modelAviona) {
        this.modelAviona = modelAviona;
    }

    public String getProizvodacAviona() {
        return proizvodacAviona;
    }

    public void setProizvodacAviona(String proizvodacAviona) {
        this.proizvodacAviona = proizvodacAviona;
    }

    @Override
    public String toString() {
        return proizvodacAviona + " " + modelAviona + " (Kapacitet: " + kapacitetAviona + ")";
    }
}