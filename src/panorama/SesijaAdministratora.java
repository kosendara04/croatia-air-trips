package panorama;

import panorama.SesijaAdministratora;

public class SesijaAdministratora {
    private static SesijaAdministratora instanca;
    private int idAdministratora;
    private String imeAdministratora;

    private SesijaAdministratora() {
        // Privatni konstruktor za sprječavanje instanciranja
    }

    public static SesijaAdministratora getInstance() {
        if (instanca == null) {
            instanca = new SesijaAdministratora();
        }
        return instanca;
    }

    public void postaviAdministratora(int idAdministratora, String imeAdministratora) {
        this.idAdministratora = idAdministratora;
        this.imeAdministratora = imeAdministratora;
    }

    public int getIdAdministratora() {
        return idAdministratora;
    }

    public String getImeAdministratora() {
        return imeAdministratora;
    }

    public void ocistiSesiju() {
        idAdministratora = 0;
        imeAdministratora = null;
    }
}