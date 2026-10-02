package panorama;

import model.Korisnik;
import model.SigurnostUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;

import static model.BazaPodataka.uspostaviVezu;

public class UpravljanjeKorisnicimaAdmin {
    public JFrame okvir;
    private JTable tablicaKorisnika;
    private JTextField txtIme, txtEmail, txtKontaktBroj;
    private JPasswordField txtLozinka;

    public UpravljanjeKorisnicimaAdmin() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Upravljanje korisnicima", 950, 580);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Upravljanje korisnicima");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        panel.add(lblNaslov, BorderLayout.NORTH);

        tablicaKorisnika = new JTable();
        tablicaKorisnika.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablicaKorisnika.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                popuniPoljaOdabranogReda();
            }
        });
        UIStil.urediTablicu(tablicaKorisnika);
        JScrollPane scrollPane = new JScrollPane(tablicaKorisnika);
        UIStil.urediScrollPanel(scrollPane);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(UIStil.BOJA_PANELA);
        formPanel.setBorder(UIStil.napraviNaslovniOkvir("Detalji korisnika"));
        formPanel.setPreferredSize(new Dimension(0, 130));

        JLabel lblIme = UIStil.urediEtiketu("Ime:");
        lblIme.setBounds(16, 28, 110, 20);
        formPanel.add(lblIme);
        txtIme = new JTextField();
        txtIme.setBounds(132, 28, 210, 26);
        UIStil.urediPolje(txtIme);
        formPanel.add(txtIme);

        JLabel lblEmail = UIStil.urediEtiketu("Email:");
        lblEmail.setBounds(16, 62, 110, 20);
        formPanel.add(lblEmail);
        txtEmail = new JTextField();
        txtEmail.setBounds(132, 62, 210, 26);
        UIStil.urediPolje(txtEmail);
        formPanel.add(txtEmail);

        JLabel lblLozinka = UIStil.urediEtiketu("Lozinka:");
        lblLozinka.setBounds(380, 28, 110, 20);
        formPanel.add(lblLozinka);
        txtLozinka = new JPasswordField();
        txtLozinka.setBounds(500, 28, 210, 26);
        txtLozinka.setToolTipText("Kod ažuriranja ostavite prazno ako ne mijenjate lozinku");
        UIStil.urediPolje(txtLozinka);
        formPanel.add(txtLozinka);

        JLabel lblKontakt = UIStil.urediEtiketu("Kontakt broj:");
        lblKontakt.setBounds(380, 62, 110, 20);
        formPanel.add(lblKontakt);
        txtKontaktBroj = new JTextField();
        txtKontaktBroj.setBounds(500, 62, 210, 26);
        UIStil.urediPolje(txtKontaktBroj);
        formPanel.add(txtKontaktBroj);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        buttonPanel.setBackground(UIStil.BOJA_POZADINE);

        JButton btnDodaj = UIStil.napraviGumb("+ Dodaj korisnika");
        btnDodaj.addActionListener(e -> dodajKorisnika());
        buttonPanel.add(btnDodaj);

        JButton btnAzuriraj = UIStil.napraviGumb("Azuriraj korisnika");
        btnAzuriraj.addActionListener(e -> azurirajKorisnika());
        btnAzuriraj.setIcon(UIStil.ucitajIkonuSkaliranu("edit.png",24,24));
        buttonPanel.add(btnAzuriraj);

        JButton btnObrisi = UIStil.napraviNaglaseniGumb("Obrisi korisnika");
        btnObrisi.addActionListener(e -> obrisiKorisnika());
        btnObrisi.setIcon(UIStil.ucitajIkonuSkaliranu("close.png",24,24));
        buttonPanel.add(btnObrisi);

        JButton btnOcisti = UIStil.napraviSekundarniGumb("Ocisti formu");
        btnOcisti.addActionListener(e -> ocistiFormu());
        btnOcisti.setIcon(UIStil.ucitajIkonuSkaliranu("refresh.png",24,24));
        buttonPanel.add(btnOcisti);

        JPanel centarPanel = new JPanel(new BorderLayout(0, 10));
        centarPanel.setBackground(UIStil.BOJA_POZADINE);
        centarPanel.add(scrollPane, BorderLayout.CENTER);
        centarPanel.add(formPanel, BorderLayout.SOUTH);
        panel.add(centarPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        ucitajKorisnike();
        return panel;
    }

    private void popuniPoljaOdabranogReda() {
        int odabraniRed = tablicaKorisnika.getSelectedRow();
        if (odabraniRed >= 0) {
            txtIme.setText(tablicaKorisnika.getValueAt(odabraniRed, 1).toString());
            txtEmail.setText(tablicaKorisnika.getValueAt(odabraniRed, 2).toString());
            txtLozinka.setText("");
            Object kontaktObj = tablicaKorisnika.getValueAt(odabraniRed, 3);
            txtKontaktBroj.setText(kontaktObj != null ? kontaktObj.toString() : "");
        }
    }

    private void ocistiFormu() {
        txtIme.setText("");
        txtEmail.setText("");
        txtLozinka.setText("");
        txtKontaktBroj.setText("");
        tablicaKorisnika.clearSelection();
    }

    private void ucitajKorisnike() {
        java.util.List<Korisnik> korisnici = Korisnik.ucitajSveKorisnike();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Ime", "Email", "Kontakt Broj"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Korisnik korisnik : korisnici) {
            model.addRow(new Object[]{
                    korisnik.getId(),
                    korisnik.getIme(),
                    korisnik.getEmail(),
                    korisnik.getKontaktBrojKorisnika()
            });
        }

        tablicaKorisnika.setModel(model);
        UIStil.urediTablicu(tablicaKorisnika);
        tablicaKorisnika.getColumnModel().getColumn(0).setPreferredWidth(50);
    }

    private void dodajKorisnika() {
        if (!validirajPoljaZaDodavanje()) return;

        try (Connection veza = uspostaviVezu()) {
            String upitProvjere = "SELECT id FROM KORISNIK WHERE email=?";
            try (PreparedStatement provjeraIzjava = veza.prepareStatement(upitProvjere)) {
                provjeraIzjava.setString(1, txtEmail.getText().trim());
                ResultSet rs = provjeraIzjava.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(okvir, "Email već postoji", "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String upit = "INSERT INTO KORISNIK (ime, email, lozinka, uloga, kontaktBroj_korisnika) VALUES (?, ?, ?, 'user', ?)";
            try (PreparedStatement izjava = veza.prepareStatement(upit)) {
                izjava.setString(1, txtIme.getText().trim());
                izjava.setString(2, txtEmail.getText().trim());
                izjava.setString(3, SigurnostUtil.hesirajLozinku(new String(txtLozinka.getPassword()).trim()));
                izjava.setString(4, txtKontaktBroj.getText().trim());

                int obradjeniRedovi = izjava.executeUpdate();
                if (obradjeniRedovi > 0) {
                    JOptionPane.showMessageDialog(okvir, "Korisnik uspješno dodan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                    ucitajKorisnike();
                    ocistiFormu();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri dodavanju korisnika: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void azurirajKorisnika() {
        int odabraniRed = tablicaKorisnika.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite korisnika za ažuriranje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validirajPoljaZaAzuriranje()) return;

        int idKorisnika = (int) tablicaKorisnika.getValueAt(odabraniRed, 0);
        String ime = txtIme.getText().trim();
        String email = txtEmail.getText().trim();
        String kontakt = txtKontaktBroj.getText().trim();
        String lozinka = new String(txtLozinka.getPassword()).trim();
        boolean mijenjaLozinku = !lozinka.isEmpty();

        String upit = mijenjaLozinku
                ? "UPDATE KORISNIK SET ime=?, email=?, lozinka=?, kontaktBroj_korisnika=? WHERE id=?"
                : "UPDATE KORISNIK SET ime=?, email=?, kontaktBroj_korisnika=? WHERE id=?";

        try (Connection veza = uspostaviVezu();
             PreparedStatement izjava = veza.prepareStatement(upit)) {

            int indeks = 1;
            izjava.setString(indeks++, ime);
            izjava.setString(indeks++, email);
            if (mijenjaLozinku) {
                izjava.setString(indeks++, SigurnostUtil.hesirajLozinku(lozinka));
            }
            izjava.setString(indeks++, kontakt);
            izjava.setInt(indeks, idKorisnika);

            int obradjeniRedovi = izjava.executeUpdate();
            if (obradjeniRedovi > 0) {
                String poruka = mijenjaLozinku ? "Korisnik uspješno ažuriran, lozinka promijenjena!" : "Korisnik uspješno ažuriran!";
                JOptionPane.showMessageDialog(okvir, poruka, "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                ucitajKorisnike();
                ocistiFormu();
            } else {
                JOptionPane.showMessageDialog(okvir, "Greška pri ažuriranju korisnika!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri ažuriranju korisnika: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisiKorisnika() {
        int odabraniRed = tablicaKorisnika.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite korisnika za brisanje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int potvrda = JOptionPane.showConfirmDialog(okvir,
                "Jeste li sigurni da želite obrisati ovog korisnika?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
        if (potvrda != JOptionPane.YES_OPTION) return;

        int idKorisnika = (int) tablicaKorisnika.getValueAt(odabraniRed, 0);

        try (Connection veza = uspostaviVezu()) {
            String upitBrisanjaRezervacija = "DELETE FROM REZERVACIJE_LETOVA WHERE korisnik_id=?";
            try (PreparedStatement izjava1 = veza.prepareStatement(upitBrisanjaRezervacija)) {
                izjava1.setInt(1, idKorisnika);
                izjava1.executeUpdate();
            }

            String upitBrisanjaKorisnika = "DELETE FROM KORISNIK WHERE id=?";
            try (PreparedStatement izjava2 = veza.prepareStatement(upitBrisanjaKorisnika)) {
                izjava2.setInt(1, idKorisnika);
                int obradjeniRedovi = izjava2.executeUpdate();
                if (obradjeniRedovi > 0) {
                    JOptionPane.showMessageDialog(okvir, "Korisnik uspješno obrisan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                    ucitajKorisnike();
                    ocistiFormu();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri brisanju korisnika: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validirajPoljaZaDodavanje() {
        if (txtIme.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                new String(txtLozinka.getPassword()).trim().isEmpty() ||
                txtKontaktBroj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Sva polja su obavezna", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!txtEmail.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(okvir, "Unesite valjanu email adresu", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (new String(txtLozinka.getPassword()).trim().length() < 6) {
            JOptionPane.showMessageDialog(okvir, "Lozinka mora imati najmanje 6 znakova", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validirajPoljaZaAzuriranje() {
        if (txtIme.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtKontaktBroj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Ime, email i kontakt broj su obavezni", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!txtEmail.getText().trim().contains("@")) {
            JOptionPane.showMessageDialog(okvir, "Unesite valjanu email adresu", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String lozinka = new String(txtLozinka.getPassword()).trim();
        if (!lozinka.isEmpty() && lozinka.length() < 6) {
            JOptionPane.showMessageDialog(okvir, "Ako mijenjate lozinku, mora imati najmanje 6 znakova", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}