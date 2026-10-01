package panorama;

import model.Korisnik;
import model.Let;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class PregledRezervacijaAdmin {

    public JFrame okvir;
    private JTable tablicaRezervacija;
    private JComboBox<String> cmbFilter;

    private JComboBox<Korisnik> cbKorisnici;
    private JComboBox<Let> cbLetovi;
    private JSpinner spinnerDatumRezervacije;
    private JSpinner spinnerBrojSjedala;

    public PregledRezervacijaAdmin() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame("Pregled rezervacija");
        UIStil.urediokvir(okvir, "Pregled rezervacija", 1100, 600);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel naslovBar = new JPanel(new BorderLayout(12, 0));
        naslovBar.setOpaque(false);

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Upravljanje rezervacijama");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        naslovBar.add(lblNaslov, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        JLabel lblFilter = UIStil.urediEtiketu("Filtriraj:");
        filterPanel.add(lblFilter);

        cmbFilter = new JComboBox<>(new String[]{
                "Sve rezervacije",
                "Današnje rezervacije",
                "Nadolazeći letovi"
        });

        UIStil.urediComboBox(cmbFilter);
        cmbFilter.setPreferredSize(new Dimension(200, 30));
        cmbFilter.addActionListener(e -> ucitajRezervacije());
        filterPanel.add(cmbFilter);

        JButton btnRefresh = UIStil.napraviGumb("↻ Osvježi");
        btnRefresh.addActionListener(e -> ucitajRezervacije());
        filterPanel.add(btnRefresh);
        naslovBar.add(filterPanel, BorderLayout.EAST);
        panel.add(naslovBar, BorderLayout.NORTH);

        tablicaRezervacija = new JTable();
        tablicaRezervacija.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablicaRezervacija.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                popuniPoljaOdabranogReda();
            }
        });

        UIStil.urediTablicu(tablicaRezervacija);
        JScrollPane scroll = new JScrollPane(tablicaRezervacija);
        UIStil.urediScrollPanel(scroll);
        scroll.setPreferredSize(new Dimension(0, 220));

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(UIStil.BOJA_PANELA);
        formPanel.setBorder(UIStil.napraviNaslovniOkvir("Detalji rezervacije"));
        formPanel.setPreferredSize(new Dimension(0, 170));

        JLabel lblUser = UIStil.urediEtiketu("Korisnik:");
        lblUser.setBounds(20, 30, 100, 22);
        formPanel.add(lblUser);

        cbKorisnici = new JComboBox<>();
        cbKorisnici.setBounds(120, 30, 220, 26);
        UIStil.urediComboBox(cbKorisnici);
        formPanel.add(cbKorisnici);

        JLabel lblFlight = UIStil.urediEtiketu("Let:");
        lblFlight.setBounds(20, 70, 100, 22);
        formPanel.add(lblFlight);

        cbLetovi = new JComboBox<>();
        cbLetovi.setBounds(120, 70, 220, 26);
        UIStil.urediComboBox(cbLetovi);
        formPanel.add(cbLetovi);

        JLabel lblSeats = UIStil.urediEtiketu("Broj sjedala:");
        lblSeats.setBounds(20, 110, 100, 22);
        formPanel.add(lblSeats);

        spinnerBrojSjedala = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spinnerBrojSjedala.setBounds(120, 110, 80, 26);
        formPanel.add(spinnerBrojSjedala);

        JLabel lblDate = UIStil.urediEtiketu("Datum rezervacije:");
        lblDate.setBounds(400, 30, 140, 22);
        formPanel.add(lblDate);

        spinnerDatumRezervacije = new JSpinner(new SpinnerDateModel());
        spinnerDatumRezervacije.setEditor(
                new JSpinner.DateEditor(spinnerDatumRezervacije, "dd.MM.yyyy HH:mm")
        );
        spinnerDatumRezervacije.setValue(new Date());
        spinnerDatumRezervacije.setBounds(550, 30, 180, 26);
        formPanel.add(spinnerDatumRezervacije);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        buttonPanel.setBackground(UIStil.BOJA_POZADINE);

        JButton btnDodaj = UIStil.napraviGumb("+ Dodaj");
        btnDodaj.addActionListener(e -> dodajRezervaciju());
        buttonPanel.add(btnDodaj);

        JButton btnAzuriraj = UIStil.napraviGumb("Ažuriraj");
        btnAzuriraj.setIcon(UIStil.ucitajIkonuSkaliranu("edit.png", 24, 24));
        btnAzuriraj.addActionListener(e -> azurirajRezervaciju());
        buttonPanel.add(btnAzuriraj);

        JButton btnObrisi = UIStil.napraviNaglaseniGumb("Obriši");
        btnObrisi.setIcon(UIStil.ucitajIkonuSkaliranu("close.png", 24, 24));
        btnObrisi.addActionListener(e -> obrisiRezervaciju());
        buttonPanel.add(btnObrisi);

        JButton btnOcisti = UIStil.napraviSekundarniGumb("Očisti");
        btnOcisti.setIcon(UIStil.ucitajIkonuSkaliranu("refresh.png", 24, 24));
        btnOcisti.addActionListener(e -> ocistiFormu());
        buttonPanel.add(btnOcisti);

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setBackground(UIStil.BOJA_POZADINE);
        center.add(scroll, BorderLayout.CENTER);
        center.add(formPanel, BorderLayout.SOUTH);

        panel.add(center, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        ucitajKorisnike();
        ucitajLetove();
        ucitajRezervacije();

        return panel;
    }

    private void ocistiFormu() {
        if (cbKorisnici.getItemCount() > 0)
            cbKorisnici.setSelectedIndex(0);

        if (cbLetovi.getItemCount() > 0)
            cbLetovi.setSelectedIndex(0);

        spinnerBrojSjedala.setValue(1);
        spinnerDatumRezervacije.setValue(new Date());
        tablicaRezervacija.clearSelection();
    }

    private void ucitajKorisnike() {
        cbKorisnici.removeAllItems();
        List<Korisnik> korisnici = Korisnik.ucitajSveKorisnike();
        for (Korisnik k : korisnici) {
            cbKorisnici.addItem(k);
        }
    }

    private void ucitajLetove() {
        cbLetovi.removeAllItems();
        List<Let> letovi = Let.dohvatiSveLetove();
        for (Let l : letovi) {
            cbLetovi.addItem(l);
        }
    }

    private void popuniPoljaOdabranogReda() {
        int red = tablicaRezervacija.getSelectedRow();
        if (red == -1)
            return;

        String korisnikIme = tablicaRezervacija.getValueAt(red, 1).toString();
        String brojLeta = tablicaRezervacija.getValueAt(red, 2).toString();

        for (int i = 0; i < cbKorisnici.getItemCount(); i++) {
            Korisnik k = cbKorisnici.getItemAt(i);
            if (k.getIme().equals(korisnikIme)) {
                cbKorisnici.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cbLetovi.getItemCount(); i++) {
            Let l = cbLetovi.getItemAt(i);
            if (l.getBrojLeta().equals(brojLeta)) {
                cbLetovi.setSelectedIndex(i);
                break;
            }
        }

        Object sjedalaVal = tablicaRezervacija.getValueAt(red, 7);
        if (sjedalaVal != null) {
            spinnerBrojSjedala.setValue(((Number) sjedalaVal).intValue());
        }

        try {
            String datum = tablicaRezervacija.getValueAt(red, 9).toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            spinnerDatumRezervacije.setValue(sdf.parse(datum));
        } catch (Exception ex) {
            spinnerDatumRezervacije.setValue(new Date());
        }
    }

    private void dodajRezervaciju() {
        Korisnik korisnik = (Korisnik) cbKorisnici.getSelectedItem();
        Let let = (Let) cbLetovi.getSelectedItem();

        if (korisnik == null || let == null) {
            JOptionPane.showMessageDialog(okvir, "Odaberite korisnika i let.");
            return;
        }

        int brojSjedala = (Integer) spinnerBrojSjedala.getValue();

        try (Connection veza = uspostaviVezu()) {
            veza.setAutoCommit(false);

            try {
                PreparedStatement psCheck = veza.prepareStatement(
                        "SELECT dostupna_sjedista FROM LETOVI WHERE let_id=?");
                psCheck.setInt(1, let.getIdLeta());
                ResultSet rs = psCheck.executeQuery();

                if (!rs.next() || rs.getInt("dostupna_sjedista") < brojSjedala) {
                    veza.rollback();
                    JOptionPane.showMessageDialog(okvir,
                            "Nema dovoljno dostupnih sjedišta na odabranom letu.",
                            "Greška", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement psInsert = veza.prepareStatement(
                        "INSERT INTO REZERVACIJE_LETOVA(korisnik_id, let_id, broj_sjedista, datum_rezervacije) VALUES(?,?,?,?)");
                psInsert.setInt(1, korisnik.getId());
                psInsert.setInt(2, let.getIdLeta());
                psInsert.setInt(3, brojSjedala);
                psInsert.setTimestamp(4, new Timestamp(((Date) spinnerDatumRezervacije.getValue()).getTime()));
                psInsert.executeUpdate();

                PreparedStatement psUpdate = veza.prepareStatement(
                        "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista - ? WHERE let_id=?");
                psUpdate.setInt(1, brojSjedala);
                psUpdate.setInt(2, let.getIdLeta());
                psUpdate.executeUpdate();

                veza.commit();
                JOptionPane.showMessageDialog(okvir, "Rezervacija uspješno dodana.");
                ucitajRezervacije();
                ucitajLetove();
                ocistiFormu();

            } catch (SQLException ex) {
                veza.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(okvir, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void azurirajRezervaciju() {
        int red = tablicaRezervacija.getSelectedRow();
        if (red == -1) {
            JOptionPane.showMessageDialog(okvir, "Odaberite rezervaciju.");
            return;
        }

        int id = (Integer) tablicaRezervacija.getValueAt(red, 0);
        Korisnik korisnik = (Korisnik) cbKorisnici.getSelectedItem();
        Let let = (Let) cbLetovi.getSelectedItem();
        int noviSjedala = (Integer) spinnerBrojSjedala.getValue();

        try (Connection veza = uspostaviVezu()) {
            veza.setAutoCommit(false);

            try {
                PreparedStatement psStari = veza.prepareStatement(
                        "SELECT broj_sjedista, let_id FROM REZERVACIJE_LETOVA WHERE rezervacija_id=?");
                psStari.setInt(1, id);
                ResultSet rs = psStari.executeQuery();

                if (!rs.next()) {
                    veza.rollback();
                    return;
                }

                int stariSjedala = rs.getInt("broj_sjedista");
                int stariLetId = rs.getInt("let_id");
                int razlika = noviSjedala - stariSjedala;

                if (razlika > 0) {
                    PreparedStatement psCheck = veza.prepareStatement(
                            "SELECT dostupna_sjedista FROM LETOVI WHERE let_id=?");
                    psCheck.setInt(1, let.getIdLeta());
                    ResultSet rsCheck = psCheck.executeQuery();

                    if (!rsCheck.next() || rsCheck.getInt("dostupna_sjedista") < razlika) {
                        veza.rollback();
                        JOptionPane.showMessageDialog(okvir,
                                "Nema dovoljno dostupnih sjedišta za ovu promjenu.",
                                "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (stariLetId != let.getIdLeta()) {
                    PreparedStatement psRestoreOld = veza.prepareStatement(
                            "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista + ? WHERE let_id=?");
                    psRestoreOld.setInt(1, stariSjedala);
                    psRestoreOld.setInt(2, stariLetId);
                    psRestoreOld.executeUpdate();

                    PreparedStatement psCheckNew = veza.prepareStatement(
                            "SELECT dostupna_sjedista FROM LETOVI WHERE let_id=?");
                    psCheckNew.setInt(1, let.getIdLeta());
                    ResultSet rsNew = psCheckNew.executeQuery();

                    if (!rsNew.next() || rsNew.getInt("dostupna_sjedista") < noviSjedala) {
                        veza.rollback();
                        JOptionPane.showMessageDialog(okvir,
                                "Nema dovoljno dostupnih sjedišta na novom letu.",
                                "Greška", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    PreparedStatement psDeductNew = veza.prepareStatement(
                            "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista - ? WHERE let_id=?");
                    psDeductNew.setInt(1, noviSjedala);
                    psDeductNew.setInt(2, let.getIdLeta());
                    psDeductNew.executeUpdate();

                } else {
                    PreparedStatement psAdjust = veza.prepareStatement(
                            "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista - ? WHERE let_id=?");
                    psAdjust.setInt(1, razlika);
                    psAdjust.setInt(2, let.getIdLeta());
                    psAdjust.executeUpdate();
                }

                PreparedStatement psUpdate = veza.prepareStatement(
                        "UPDATE REZERVACIJE_LETOVA SET korisnik_id=?, let_id=?, broj_sjedista=?, datum_rezervacije=? WHERE rezervacija_id=?");
                psUpdate.setInt(1, korisnik.getId());
                psUpdate.setInt(2, let.getIdLeta());
                psUpdate.setInt(3, noviSjedala);
                psUpdate.setTimestamp(4, new Timestamp(((Date) spinnerDatumRezervacije.getValue()).getTime()));
                psUpdate.setInt(5, id);
                psUpdate.executeUpdate();

                veza.commit();
                JOptionPane.showMessageDialog(okvir, "Rezervacija ažurirana.");
                ucitajRezervacije();
                ucitajLetove();

            } catch (SQLException ex) {
                veza.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(okvir, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisiRezervaciju() {
        int red = tablicaRezervacija.getSelectedRow();
        if (red == -1) {
            JOptionPane.showMessageDialog(okvir, "Odaberite rezervaciju.");
            return;
        }

        int potvrda = JOptionPane.showConfirmDialog(okvir, "Obrisati rezervaciju?", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (potvrda != JOptionPane.YES_OPTION)
            return;

        int id = (Integer) tablicaRezervacija.getValueAt(red, 0);

        try (Connection veza = uspostaviVezu()) {
            veza.setAutoCommit(false);

            try {
                PreparedStatement psGet = veza.prepareStatement(
                        "SELECT broj_sjedista, let_id FROM REZERVACIJE_LETOVA WHERE rezervacija_id=?");
                psGet.setInt(1, id);
                ResultSet rs = psGet.executeQuery();

                if (!rs.next()) {
                    veza.rollback();
                    return;
                }

                int sjedala = rs.getInt("broj_sjedista");
                int letId = rs.getInt("let_id");

                PreparedStatement psDelete = veza.prepareStatement(
                        "DELETE FROM REZERVACIJE_LETOVA WHERE rezervacija_id=?");
                psDelete.setInt(1, id);
                psDelete.executeUpdate();

                PreparedStatement psRestore = veza.prepareStatement(
                        "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista + ? WHERE let_id=?");
                psRestore.setInt(1, sjedala);
                psRestore.setInt(2, letId);
                psRestore.executeUpdate();

                veza.commit();
                JOptionPane.showMessageDialog(okvir, "Rezervacija obrisana.");
                ucitajRezervacije();
                ucitajLetove();
                ocistiFormu();

            } catch (SQLException ex) {
                veza.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(okvir, ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ucitajRezervacije() {
        try (Connection veza = uspostaviVezu()) {

            String upit = "SELECT " +
                    "r.rezervacija_id, " +
                    "u.ime AS user_name, " +
                    "l.broj_leta, " +
                    "l.grad_polaska, " +
                    "l.grad_dolaska, " +
                    "l.vrijeme_polaska, " +
                    "l.vrijeme_dolaska, " +
                    "r.broj_sjedista, " +
                    "a.model_aviona, " +
                    "r.datum_rezervacije " +
                    "FROM REZERVACIJE_LETOVA r " +
                    "JOIN KORISNIK u ON r.korisnik_id = u.id " +
                    "JOIN LETOVI l ON r.let_id = l.let_id " +
                    "JOIN AVION a ON l.id_avion = a.id_aviona ";

            String filter = (String) cmbFilter.getSelectedItem();
            if ("Današnje rezervacije".equals(filter)) {
                upit += "WHERE DATE(r.datum_rezervacije) = CURRENT_DATE ";
            } else if ("Nadolazeći letovi".equals(filter)) {
                upit += "WHERE l.vrijeme_polaska > CURRENT_TIMESTAMP ";
            }

            upit += "ORDER BY r.datum_rezervacije DESC";

            PreparedStatement izjava = veza.prepareStatement(upit);
            ResultSet rezultati = izjava.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new Object[]{"ID", "Korisnik", "Broj leta", "Polazak", "Odredište", "Vrijeme polaska", "Vrijeme dolaska", "Broj sjedala", "Avion", "Datum rezervacije"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rezultati.next()) {
                model.addRow(new Object[]{
                        rezultati.getInt("rezervacija_id"),
                        rezultati.getString("user_name"),
                        rezultati.getString("broj_leta"),
                        rezultati.getString("grad_polaska"),
                        rezultati.getString("grad_dolaska"),
                        rezultati.getTimestamp("vrijeme_polaska") != null
                                ? dateFormat.format(rezultati.getTimestamp("vrijeme_polaska")) : "",
                        rezultati.getTimestamp("vrijeme_dolaska") != null
                                ? dateFormat.format(rezultati.getTimestamp("vrijeme_dolaska")) : "",
                        rezultati.getInt("broj_sjedista"),
                        rezultati.getString("model_aviona"),
                        rezultati.getTimestamp("datum_rezervacije") != null
                                ? dateFormat.format(rezultati.getTimestamp("datum_rezervacije")) : ""
                });
            }

            tablicaRezervacija.setModel(model);
            UIStil.urediTablicu(tablicaRezervacija);
            tablicaRezervacija.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablicaRezervacija.getColumnModel().getColumn(7).setPreferredWidth(80);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri učitavanju rezervacija: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }
}