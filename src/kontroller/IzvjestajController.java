package kontroller;

import model.Izvjestaj;
import panorama.GlavniIzbornikAdministrator;
import panorama.PregledIzvjestaja;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class IzvjestajController {

    private PregledIzvjestaja view;

    public IzvjestajController(PregledIzvjestaja view) {
        this.view = view;

        loadReports();

        this.view.getBtnOdjava().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GlavniIzbornikAdministrator().getOkvir().setVisible(true);
                view.getOkvir().dispose();
            }
        });
    }

    private void loadReports() {
        try {
            List<Izvjestaj> izvjestaji = Izvjestaj.getAllIzvjestaj();
            view.setReportData(izvjestaji);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Greška pri učitavanju izvještaja: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}
