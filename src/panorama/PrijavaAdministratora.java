package panorama;

import model.Administrator;

import javax.swing.*;
import java.awt.*;

public class PrijavaAdministratora {
    public JFrame okvir;
    private JTextField txtKorisnickoIme;
    private JPasswordField txtLozinka;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PrijavaAdministratora prozor = new PrijavaAdministratora();
                prozor.okvir.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PrijavaAdministratora() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Prijava administratora", 520, 460);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());

        JPanel panelZaglavlja = new JPanel(new BorderLayout());
        panelZaglavlja.setBackground(UIStil.BOJA_ZAGLAVLJA_TAB);
        panelZaglavlja.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        JPanel naslovPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        naslovPanel.setOpaque(false);

        ImageIcon ikonaAdmin = UIStil.ucitajIkonuSkaliranu("user-avatar.png", 28, 28);
        if (ikonaAdmin != null) {
            naslovPanel.add(new JLabel(ikonaAdmin));
        }

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Prijava administratora");
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
        kartica.add(UIStil.urediEtiketu("Korisničko ime:"), gbc);

        gbc.gridy = 1;
        txtKorisnickoIme = UIStil.urediPolje(new JTextField());
        kartica.add(txtKorisnickoIme, gbc);

        gbc.gridy = 2;
        kartica.add(UIStil.urediEtiketu("Lozinka:"), gbc);

        gbc.gridy = 3;
        txtLozinka = UIStil.urediLozinkuPolje(new JPasswordField());
        kartica.add(txtLozinka, gbc);

        gbc.gridy = 4;
        JButton btnPrijava = UIStil.napraviGumb("Prijava");
        btnPrijava.addActionListener(e -> prijaviAdministratora());
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

    private void prijaviAdministratora() {
        String korisnickoIme = txtKorisnickoIme.getText().trim();
        String lozinka = new String(txtLozinka.getPassword()).trim();

        if (korisnickoIme.isEmpty() || lozinka.isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Sva polja su obavezna!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Administrator administrator = Administrator.prijaviAdministratora(korisnickoIme, lozinka);

        if (administrator != null) {
            SesijaAdministratora sesija = SesijaAdministratora.getInstance();
            sesija.postaviAdministratora(administrator.getId(), administrator.getIme());

            GlavniIzbornikAdministrator glavniIzbornik = new GlavniIzbornikAdministrator();
            glavniIzbornik.getOkvir().setVisible(true);
            okvir.dispose();
        } else {
            JOptionPane.showMessageDialog(okvir, "Neispravno korisničko ime ili lozinka",
                    "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}