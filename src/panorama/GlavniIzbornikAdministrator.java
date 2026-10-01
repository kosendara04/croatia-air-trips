package panorama;

import javax.swing.*;
import java.awt.*;

public class GlavniIzbornikAdministrator {
    JFrame okvir;

    public GlavniIzbornikAdministrator() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Administratorski izbornik", 1180, 700);
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
        JLabel lblAdmin = new JLabel("  |  Administrator");
        lblAdmin.setFont(UIStil.FONT_TEKST);
        lblAdmin.setForeground(UIStil.BOJA_TEKSTA_SLABIJA);
        lijevoPanel.add(lblAdmin);
        zaglavlje.add(lijevoPanel, BorderLayout.WEST);

        JPanel desnoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        desnoPanel.setOpaque(false);

        SesijaAdministratora sesija = SesijaAdministratora.getInstance();
        ImageIcon ikonaAdmin = UIStil.ucitajIkonuSkaliranu("settings.png", 20, 20);
        JLabel lblAdministrator = new JLabel("  " + sesija.getImeAdministratora());
        lblAdministrator.setFont(UIStil.FONT_TEKST);
        lblAdministrator.setForeground(UIStil.BOJA_NAGLASKA);
        if (ikonaAdmin != null) lblAdministrator.setIcon(ikonaAdmin);
        desnoPanel.add(lblAdministrator);

        JButton btnOdjava = UIStil.napraviSekundarniGumb("Odjava");
        btnOdjava.setPreferredSize(new Dimension(90, 30));
        btnOdjava.addActionListener(e -> {
            SesijaAdministratora.getInstance().ocistiSesiju();
            GlavnaStr glavnaStranica = new GlavnaStr();
            glavnaStranica.okvir.setVisible(true);
            okvir.dispose();
            JOptionPane.showMessageDialog(null, "Uspješno ste se odjavili");
        });
        desnoPanel.add(btnOdjava);
        zaglavlje.add(desnoPanel, BorderLayout.EAST);

        return zaglavlje;
    }

    private JTabbedPane napraviTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        UIStil.urediTabbedPane(tabbedPane);
        tabbedPane.setBackground(UIStil.BOJA_POZADINE);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        ImageIcon ikonaLet = UIStil.ucitajIkonuSkaliranu("home (2).png", 18, 18);
        ImageIcon ikonaAvion = UIStil.ucitajIkonuSkaliranu("note.png", 18, 18);
        ImageIcon ikonaKorisnik = UIStil.ucitajIkonuSkaliranu("user.png", 18, 18);
        ImageIcon ikonaRezervacija = UIStil.ucitajIkonuSkaliranu("reservation.png", 18, 18);
        ImageIcon ikonaIzvjestaj = UIStil.ucitajIkonuSkaliranu("report.png", 18, 18);

        UpravljanjeLetovimaAdmin upravljanjeLetovima = new UpravljanjeLetovimaAdmin();
        tabbedPane.addTab("  Upravljanje letovima  ", ikonaLet, upravljanjeLetovima.getPanel(), "Upravljanje letovima");

        UpravljanjeAvionimaAdmin upravljanjeAvionima = new UpravljanjeAvionimaAdmin();
        tabbedPane.addTab("  Upravljanje avionima  ", ikonaAvion, upravljanjeAvionima.getPanel(), "Upravljanje avionima");

        UpravljanjeKorisnicimaAdmin upravljanjeKorisnicima = new UpravljanjeKorisnicimaAdmin();
        tabbedPane.addTab("  Upravljanje korisnicima  ", ikonaKorisnik, upravljanjeKorisnicima.getPanel(), "Upravljanje korisnicima");

        PregledRezervacijaAdmin pregledRezervacija = new PregledRezervacijaAdmin();
        tabbedPane.addTab("  Pregled rezervacija  ", ikonaRezervacija, pregledRezervacija.getPanel(), "Pregled svih rezervacija");

        PregledIzvjestaja pregledIzvjestaja = new PregledIzvjestaja();
        tabbedPane.addTab("  Izvještaji  ", ikonaIzvjestaj, pregledIzvjestaja.getPanel(), "Izvještaji sustava");

        return tabbedPane;
    }

    public JFrame getOkvir() {
        return okvir;
    }
}