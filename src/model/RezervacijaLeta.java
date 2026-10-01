package model;

import java.sql.Timestamp;

public class RezervacijaLeta {
    private int idRezervacije;
    private int idKorisnika;
    private int idLeta;
    private Timestamp datumRezervacije;
    private int brojMjesta;
    
    // Za spajanje s drugim tablicama
    private Korisnik korisnik;
    private Let let;
    
    // Konstruktori
    public RezervacijaLeta() {
    }
    
    public RezervacijaLeta(int idRezervacije, int idKorisnika, int idLeta, Timestamp datumRezervacije, int brojMjesta) {
        this.idRezervacije = idRezervacije;
        this.idKorisnika = idKorisnika;
        this.idLeta = idLeta;
        this.datumRezervacije = datumRezervacije;
        this.brojMjesta = brojMjesta;
    }
    
    // Getteri i Setteri
    public int getIdRezervacije() {
        return idRezervacije;
    }
    
    public void setIdRezervacije(int idRezervacije) {
        this.idRezervacije = idRezervacije;
    }
    
    public int getIdKorisnika() {
        return idKorisnika;
    }
    
    public void setIdKorisnika(int idKorisnika) {
        this.idKorisnika = idKorisnika;
    }
    
    public int getIdLeta() {
        return idLeta;
    }
    
    public void setIdLeta(int idLeta) {
        this.idLeta = idLeta;
    }
    
    public Timestamp getDatumRezervacije() {
        return datumRezervacije;
    }
    
    public void setDatumRezervacije(Timestamp datumRezervacije) {
        this.datumRezervacije = datumRezervacije;
    }
    
    public int getBrojMjesta() {
        return brojMjesta;
    }
    
    public void setBrojMjesta(int brojMjesta) {
        this.brojMjesta = brojMjesta;
    }
    
    public Korisnik getKorisnik() {
        return korisnik;
    }
    
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    
    public Let getLet() {
        return let;
    }
    
    public void setLet(Let let) {
        this.let = let;
    }
    
    @Override
    public String toString() {
        return "RezervacijaLeta{" +
                "idRezervacije=" + idRezervacije +
                ", idKorisnika=" + idKorisnika +
                ", idLeta=" + idLeta +
                ", datumRezervacije=" + datumRezervacije +
                ", brojMjesta=" + brojMjesta +
                '}';
    }
}
