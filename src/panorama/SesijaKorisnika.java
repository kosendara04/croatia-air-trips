package panorama;

import panorama.SesijaKorisnika;

public class SesijaKorisnika {
    private static SesijaKorisnika instanca;
    private int idKorisnika;
    private String korisnickoIme;

    private SesijaKorisnika() {
        // Privatni konstruktor za sprječavanje instanciranja
    }

    public static SesijaKorisnika getInstance() {
        if (instanca == null) {
            instanca = new SesijaKorisnika();
        }
        return instanca;
    }

    public void postaviKorisnika(int idKorisnika, String korisnickoIme) {
        this.idKorisnika = idKorisnika;
        this.korisnickoIme = korisnickoIme;
    }

    public int getIdKorisnika() {
        return idKorisnika;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void ocistiSesiju() {
        idKorisnika = 0;
        korisnickoIme = null;
    }
}