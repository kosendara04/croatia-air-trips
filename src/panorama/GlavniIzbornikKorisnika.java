package panorama;

import kontroller.PovijestRezervacijaController;

import java.awt.*;
import javax.swing.*;

public class GlavniIzbornikKorisnika {

    JFrame okvir;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GlavniIzbornikKorisnika prozor = new GlavniIzbornikKorisnika();
                prozor.okvir.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GlavniIzbornikKorisnika() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Korisnički izbornik", 1000, 660);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout(0, 0));

        okvir.add(napraviZaglavlje(), BorderLayout.NORTH);
        okvir.add(napraviTabbedPane(), BorderLayout.CENTER);
    }

    private JPanel napraviZaglavlje() {
        JPanel zaglavlje = new JPanel(new BorderLayout());
        zaglavlje.setBackground(UIStil.BOJA_ZAGLAVLJA_TAB);
        zaglavlje.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JPanel lijevoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        lijevoPanel.setOpaque(false);
        ImageIcon ikonaKuca = UIStil.ucitajIkonuSkaliranu("home (2).png", 24, 24);
        if (ikonaKuca != null) lijevoPanel.add(new JLabel(ikonaKuca));
        JLabel lblApp = new JLabel("Croatia Air Trips");
        lblApp.setFont(UIStil.FONT_PODNASLOV);
        lblApp.setForeground(UIStil.BOJA_TEKSTA);
        lijevoPanel.add(lblApp);
        zaglavlje.add(lijevoPanel, BorderLayout.WEST);

        JPanel desnoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        desnoPanel.setOpaque(false);

        SesijaKorisnika sesija = SesijaKorisnika.getInstance();
        ImageIcon ikonaKorisnik = UIStil.ucitajIkonuSkaliranu("user-avatar.png", 20, 20);
        JLabel lblKorisnik = new JLabel("  " + (sesija.getKorisnickoIme() != null ? sesija.getKorisnickoIme() : "Gost"));
        lblKorisnik.setFont(UIStil.FONT_TEKST);
        lblKorisnik.setForeground(UIStil.BOJA_NAGLASKA);
        if (ikonaKorisnik != null) lblKorisnik.setIcon(ikonaKorisnik);
        desnoPanel.add(lblKorisnik);

        JButton btnOdjava = UIStil.napraviSekundarniGumb("Odjava");
        btnOdjava.setPreferredSize(new Dimension(90, 30));
        btnOdjava.addActionListener(e -> {
            SesijaKorisnika.getInstance().ocistiSesiju();
            PrijavaKorisnika prijava = new PrijavaKorisnika();
            prijava.okvir.setVisible(true);
            okvir.dispose();
            JOptionPane.showMessageDialog(null, "Uspješno ste se odjavili");
        });
        desnoPanel.add(btnOdjava);
        zaglavlje.add(desnoPanel, BorderLayout.EAST);

        return zaglavlje;
    }

    private JTabbedPane napraviTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        UIStil.urediTabbedPane(tabbedPane);
        tabbedPane.setBackground(UIStil.BOJA_POZADINE);

        ImageIcon ikonaLet = UIStil.ucitajIkonuSkaliranu("home (2).png", 18, 18);
        ImageIcon ikonaRezervacija = UIStil.ucitajIkonuSkaliranu("reservation.png", 18, 18);
        ImageIcon ikonaPovijest = UIStil.ucitajIkonuSkaliranu("clipboard.png", 18, 18);

        PregledLetovaKorisnik pregledLetova = new PregledLetovaKorisnik();
        tabbedPane.addTab("  Pregled letova  ", ikonaLet, pregledLetova.getPanel(), "Pregled dostupnih letova");

        RezervacijaLeta rezervacija = new RezervacijaLeta();
        tabbedPane.addTab("  Rezerviraj let  ", ikonaRezervacija, rezervacija.getPanel(), "Rezervacija leta");

        PovijestRezervacija povijest = new PovijestRezervacija();
        new PovijestRezervacijaController(povijest);
        if (povijest.getBtnNatrag() != null) {
            for (java.awt.event.ActionListener al : povijest.getBtnNatrag().getActionListeners()) {
                povijest.getBtnNatrag().removeActionListener(al);
            }
            povijest.getBtnNatrag().setVisible(false);
        }
        tabbedPane.addTab("  Povijest rezervacija  ", ikonaPovijest, povijest.getPanel(), "Povijest rezervacija");

        return tabbedPane;
    }

    public JFrame getOkvir() {
        return okvir;
    }
}
