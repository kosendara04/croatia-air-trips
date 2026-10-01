package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class Let {
    private int idLeta;
    private String brojLeta;
    private String gradPolaska;
    private String gradDolaska;
    private Timestamp vrijemePolaska;
    private Timestamp vrijemeDolaska;
    private int dostupnaMjesta;
    private int idAviona;
    private String modelAviona;
    
    public Let() {
    }

    public Let(int idLeta, String brojLeta, String gradPolaska, String gradDolaska,
               Timestamp vrijemePolaska, Timestamp vrijemeDolaska, int dostupnaMjesta) {
        this.idLeta = idLeta;
        this.brojLeta = brojLeta;
        this.gradPolaska = gradPolaska;
        this.gradDolaska = gradDolaska;
        this.vrijemePolaska = vrijemePolaska;
        this.vrijemeDolaska = vrijemeDolaska;
        this.dostupnaMjesta = dostupnaMjesta;
    }

    public Let(int idLeta, String brojLeta, String gradPolaska, String gradDolaska,
               Timestamp vrijemePolaska, Timestamp vrijemeDolaska, int dostupnaMjesta, int idAviona) {
        this.idLeta = idLeta;
        this.brojLeta = brojLeta;
        this.gradPolaska = gradPolaska;
        this.gradDolaska = gradDolaska;
        this.vrijemePolaska = vrijemePolaska;
        this.vrijemeDolaska = vrijemeDolaska;
        this.dostupnaMjesta = dostupnaMjesta;
        this.idAviona = idAviona;
    }

    public Let(int idLeta,
               String brojLeta,
               String gradPolaska,
               String gradDolaska,
               Timestamp vrijemePolaska,
               Timestamp vrijemeDolaska,
               int dostupnaMjesta,
               int idAviona,
               String modelAviona) {

        this.idLeta = idLeta;
        this.brojLeta = brojLeta;
        this.gradPolaska = gradPolaska;
        this.gradDolaska = gradDolaska;
        this.vrijemePolaska = vrijemePolaska;
        this.vrijemeDolaska = vrijemeDolaska;
        this.dostupnaMjesta = dostupnaMjesta;
        this.idAviona = idAviona;
        this.modelAviona = modelAviona;
    }
    
    
 // public static void alterTable() {
    //	try {
    
    		//  String sql1 = "ALTER TABLE REZERVACIJE_LETOVA ADD COLUMN Temp_Ime_Korisnika VARCHAR(100)";
      		//Connection conn1 = uspostaviVezu();
      		//Statement stat = conn1.createStatement();
      		//stat.executeUpdate(sql1);
      		
     
    		
    	//	} catch(SQLException e) {
    		//	e.printStackTrace();
    		//}
   // }
    
    
    //public static void main(String[]args){ 
    	//alterTable();
  //  }


    public static List<Let> dohvatiSveLetove() {
        List<Let> letovi = new ArrayList<>();
        String upit = "SELECT * FROM LETOVI";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit);
             ResultSet rezultati = izjava.executeQuery()) {

            while (rezultati.next()) {
                letovi.add(new Let(
                        rezultati.getInt("let_id"),
                        rezultati.getString("broj_leta"),
                        rezultati.getString("grad_polaska"),
                        rezultati.getString("grad_dolaska"),
                        rezultati.getTimestamp("vrijeme_polaska"),
                        rezultati.getTimestamp("vrijeme_dolaska"),
                        rezultati.getInt("dostupna_sjedista"),
                        rezultati.getInt("id_avion")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return letovi;
    }

    public static List<Let> dohvatiSveLetoveKorsinik() {

        List<Let> letovi = new ArrayList<>();

        String upit =
                "SELECT l.*, a.model_aviona " +
                        "FROM LETOVI l " +
                        "JOIN AVION a ON l.id_avion = a.id_aviona";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit);
             ResultSet rezultati = izjava.executeQuery()) {

            while (rezultati.next()) {

                Let let = new Let(
                        rezultati.getInt("let_id"),
                        rezultati.getString("broj_leta"),
                        rezultati.getString("grad_polaska"),
                        rezultati.getString("grad_dolaska"),
                        rezultati.getTimestamp("vrijeme_polaska"),
                        rezultati.getTimestamp("vrijeme_dolaska"),
                        rezultati.getInt("dostupna_sjedista"),
                        rezultati.getInt("id_avion")
                );

                let.setModelAviona(rezultati.getString("model_aviona"));

                letovi.add(let);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return letovi;
    }

    public static List<Let> dohvatiSveLetoves() {
        List<Let> letovi = new ArrayList<>();
        String upit = "SELECT * FROM LETOVI ORDER BY vrijeme_polaska";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit);
             ResultSet rezultati = izjava.executeQuery()) {

            while (rezultati.next()) {
                letovi.add(new Let(
                        rezultati.getInt("let_id"),
                        rezultati.getString("broj_leta"),
                        rezultati.getString("grad_polaska"),
                        rezultati.getString("grad_dolaska"),
                        rezultati.getTimestamp("vrijeme_polaska"),
                        rezultati.getTimestamp("vrijeme_dolaska"),
                        rezultati.getInt("dostupna_sjedista"),
                        rezultati.getInt("id_avion")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return letovi;
    }


    // Getteri i Setteri
    public int getIdLeta() {
        return idLeta;
    }

    public void setIdLeta(int idLeta) {
        this.idLeta = idLeta;
    }

    public String getBrojLeta() {
        return brojLeta;
    }

    public void setBrojLeta(String brojLeta) {
        this.brojLeta = brojLeta;
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

    public Timestamp getVrijemeDolaska() {
        return vrijemeDolaska;
    }

    public void setVrijemeDolaska(Timestamp vrijemeDolaska) {
        this.vrijemeDolaska = vrijemeDolaska;
    }

    public int getDostupnaMjesta() {
        return dostupnaMjesta;
    }

    public void setDostupnaMjesta(int dostupnaMjesta) {
        this.dostupnaMjesta = dostupnaMjesta;
    }

    public int getIdAviona() {
        return idAviona;
    }

    public void setIdAviona(int idAviona) {
        this.idAviona = idAviona;
    }

    public String getModelAviona() {
        return modelAviona;
    }

    public void setModelAviona(String modelAviona) {
        this.modelAviona = modelAviona;
    }

    @Override
    public String toString() {
        return brojLeta + " (" + gradPolaska + " → " + gradDolaska + ")";
    }
//
//    @Override
//    public String toString() {
//        return "Let{" +
//                "idLeta=" + idLeta +
//                ", brojLeta='" + brojLeta + '\'' +
//                ", gradPolaska='" + gradPolaska + '\'' +
//                ", gradDolaska='" + gradDolaska + '\'' +
//                ", vrijemePolaska=" + vrijemePolaska +
//                ", vrijemeDolaska=" + vrijemeDolaska +
//                ", dostupnaMjesta=" + dostupnaMjesta +
//                ", idAviona=" + idAviona +
//                '}';
//    }

}