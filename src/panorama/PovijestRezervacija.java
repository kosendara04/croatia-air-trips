package panorama;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;


public class PovijestRezervacija {

    JFrame okvir;
    private JTable tablicaRezervacija;
    JButton btnNatrag;
    private JPanel panel;
    private JButton btnOsvjezi;

    public PovijestRezervacija() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Povijest rezervacija", 900, 520);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        if (panel == null) {
            panel = new JPanel(new BorderLayout(0, 12));
            panel.setBackground(UIStil.BOJA_POZADINE);
            panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Povijest rezervacija");
            panel.add(lblNaslov, BorderLayout.NORTH);

            tablicaRezervacija = new JTable();
            UIStil.urediTablicu(tablicaRezervacija);

            JScrollPane scrollPanel = new JScrollPane(tablicaRezervacija);
            panel.add(scrollPanel, BorderLayout.CENTER);

            btnNatrag = UIStil.napraviSekundarniGumb("← Povratak na izbornik");

            btnOsvjezi = UIStil.napraviSekundarniGumb("Osvježi");

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bottom.setBackground(UIStil.BOJA_POZADINE);

            bottom.add(btnNatrag);
            bottom.add(btnOsvjezi);

            panel.add(bottom, BorderLayout.SOUTH);
        }

        return panel;
    }

    public void prikazi() {
        okvir.setVisible(true);
    }

    public void zatvori() {
        okvir.dispose();
    }

    public JButton getBtnNatrag() {
        return btnNatrag;
    }

    public JFrame getOkvir() {
        return okvir;
    }

    public JButton getBtnOsvjezi() {
        return btnOsvjezi;
    }

    public void setPodaciUTablicu(java.util.List<String[]> rezervacije) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID rezervacije");
        model.addColumn("Broj leta");
        model.addColumn("Polazak");
        model.addColumn("Odredište");
        model.addColumn("Vrijeme polaska");
        model.addColumn("Rezervirana sjedala");
        model.addColumn("Avion");
        model.addColumn("Datum rezervacije");

        for (String[] rezervacija : rezervacije) {
            model.addRow(rezervacija);
        }

        tablicaRezervacija.setModel(model);
    }
}