package kontroller;

import model.PovijestRezervacijaModel;
import panorama.GlavniIzbornikKorisnika;
import panorama.PovijestRezervacija;
import panorama.SesijaKorisnika;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

public class PovijestRezervacijaController {

    private PovijestRezervacija view;

    public PovijestRezervacijaController(PovijestRezervacija view) {
        this.view = view;
        inicijaliziraj();
    }

    private void ucitajRezervacije() {
        SesijaKorisnika sesija = SesijaKorisnika.getInstance();
        int idKorisnika = sesija.getIdKorisnika();

        if (idKorisnika == 0) {
            JOptionPane.showMessageDialog(null, "Molimo prijavite se prvo.");
            return;
        }

        try {
            List<String[]> rezervacije =
                    PovijestRezervacijaModel.getPovijestRezervacija(idKorisnika);

            SwingUtilities.invokeLater(() ->
                    view.setPodaciUTablicu(rezervacije));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Greška pri dohvaćanju rezervacija:\n" + e.getMessage());
        }
    }

    private void inicijaliziraj() {

        ucitajRezervacije();

        view.getBtnOsvjezi().addActionListener(e -> ucitajRezervacije());

        view.getOkvir().addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                ucitajRezervacije();
            }
        });
    }
}
