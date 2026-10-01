package panorama;

import model.Korisnik;

import java.awt.*;
import javax.swing.*;

public class PrijavaKorisnika {

    public JFrame okvir;
    private JTextField poljeEmail;
    private JPasswordField poljeLozinka;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PrijavaKorisnika prozor = new PrijavaKorisnika();
                prozor.okvir.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PrijavaKorisnika() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Prijava korisnika", 520, 460);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());

        JPanel panelZaglavlja = new JPanel(new BorderLayout());
        panelZaglavlja.setBackground(UIStil.BOJA_ZAGLAVLJA_TAB);
        panelZaglavlja.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        JPanel naslovPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        naslovPanel.setOpaque(false);

        ImageIcon ikonaKorisnik = UIStil.ucitajIkonuSkaliranu("user.png", 28, 28);
        if (ikonaKorisnik != null) {
            naslovPanel.add(new JLabel(ikonaKorisnik));
        }

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Prijava korisnika");
        naslovPanel.add(lblNaslov);

        panelZaglavlja.add(naslovPanel, BorderLayout.CENTER);
        okvir.add(panelZaglavlja, BorderLayout.NORTH);

        JPanel centralniPanel = new JPanel(new GridBagLayout());
        centralniPanel.setBackground(UIStil.BOJA_POZADINE);

        JPanel kartica = new JPanel(new GridBagLayout());
        kartica.setBackground(UIStil.BOJA_PANELA);
        kartica.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStil.BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(28, 36, 28, 36)
        ));
        kartica.setPreferredSize(new Dimension(360, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(6, 0, 6, 0);

        gbc.gridy = 0;
        kartica.add(UIStil.urediEtiketu("E-mail adresa:"), gbc);

        gbc.gridy = 1;
        poljeEmail = UIStil.urediPolje(new JTextField());
        kartica.add(poljeEmail, gbc);

        gbc.gridy = 2;
        kartica.add(UIStil.urediEtiketu("Lozinka:"), gbc);

        gbc.gridy = 3;
        poljeLozinka = UIStil.urediLozinkuPolje(new JPasswordField());
        kartica.add(poljeLozinka, gbc);

        gbc.gridy = 4;
        JButton btnPrijava = UIStil.napraviGumb("Prijava");
        btnPrijava.addActionListener(e -> prijaviKorisnika());
        kartica.add(btnPrijava, gbc);

        centralniPanel.add(kartica);
        okvir.add(centralniPanel, BorderLayout.CENTER);

        JPanel panelDno = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDno.setBackground(UIStil.BOJA_POZADINE);
        panelDno.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JButton btnNatrag = UIStil.napraviSekundarniGumb("← Natrag");
        btnNatrag.setPreferredSize(new Dimension(120, 34));
        btnNatrag.addActionListener(e -> {
            okvir.dispose();
            GlavnaStr glavnaStranica = new GlavnaStr();
            glavnaStranica.okvir.setVisible(true);
        });

        panelDno.add(btnNatrag);
        okvir.add(panelDno, BorderLayout.SOUTH);
    }

    private void prijaviKorisnika() {
        String email = poljeEmail.getText().trim();
        String unesenaLozinka = new String(poljeLozinka.getPassword()).trim();

        if (email.isEmpty() || unesenaLozinka.isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Unesite email i lozinku",
                    "Greška pri prijavi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Korisnik korisnik = Korisnik.prijaviKorisnika(email, unesenaLozinka);

        if (korisnik != null) {
            SesijaKorisnika sesija = SesijaKorisnika.getInstance();
            sesija.postaviKorisnika(korisnik.getId(), korisnik.getIme());

            GlavniIzbornikKorisnika glavniIzbornik = new GlavniIzbornikKorisnika();
            glavniIzbornik.okvir.setVisible(true);
            okvir.dispose();
        } else {
            JOptionPane.showMessageDialog(okvir,
                    "Neispravan email ili lozinka",
                    "Greška pri prijavi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}