package panorama;

import model.Let;
import model.Avion;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import java.util.Date;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class UpravljanjeLetovimaAdmin {
    JFrame okvir;
    private JTable tablicaLeta;
    private JTextField txtBrojLeta, txtPolazniGrad, txtOdredisniGrad, txtBrojSjedala;
    private JSpinner spinnerVrijemePolaska, spinnerVrijemeDolaska;
    private JComboBox<Avion> cbAvioni;

    public UpravljanjeLetovimaAdmin() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Upravljanje letovima", 1050, 700);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Upravljanje letovima");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        panel.add(lblNaslov, BorderLayout.NORTH);

        tablicaLeta = new JTable();
        tablicaLeta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablicaLeta.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                popuniPoljaOdabranogReda();
            }
        });
        UIStil.urediTablicu(tablicaLeta);
        JScrollPane scrollPane = new JScrollPane(tablicaLeta);
        UIStil.urediScrollPanel(scrollPane);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(UIStil.BOJA_PANELA);
        formPanel.setBorder(UIStil.napraviNaslovniOkvir("Detalji leta"));
        formPanel.setPreferredSize(new Dimension(0, 190));

        JLabel lblBrojLeta = UIStil.urediEtiketu("Broj leta:");
        lblBrojLeta.setBounds(16, 28, 110, 20);
        formPanel.add(lblBrojLeta);
        txtBrojLeta = new JTextField();
        txtBrojLeta.setBounds(132, 28, 160, 26);
        UIStil.urediPolje(txtBrojLeta);
        formPanel.add(txtBrojLeta);

        JLabel lblPolazniGrad = UIStil.urediEtiketu("Polazni grad:");
        lblPolazniGrad.setBounds(16, 62, 110, 20);
        formPanel.add(lblPolazniGrad);
        txtPolazniGrad = new JTextField();
        txtPolazniGrad.setBounds(132, 62, 160, 26);
        UIStil.urediPolje(txtPolazniGrad);
        formPanel.add(txtPolazniGrad);

        JLabel lblOdredisniGrad = UIStil.urediEtiketu("Odredišni grad:");
        lblOdredisniGrad.setBounds(16, 96, 110, 20);
        formPanel.add(lblOdredisniGrad);
        txtOdredisniGrad = new JTextField();
        txtOdredisniGrad.setBounds(132, 96, 160, 26);
        UIStil.urediPolje(txtOdredisniGrad);
        formPanel.add(txtOdredisniGrad);

        JLabel lblVrijemePolaska = UIStil.urediEtiketu("Vrijeme polaska:");
        lblVrijemePolaska.setBounds(320, 28, 120, 20);
        formPanel.add(lblVrijemePolaska);
        spinnerVrijemePolaska = new JSpinner(new SpinnerDateModel());
        spinnerVrijemePolaska.setBounds(448, 28, 170, 26);
        spinnerVrijemePolaska.setEditor(new JSpinner.DateEditor(spinnerVrijemePolaska, "dd.MM.yyyy HH:mm"));
        spinnerVrijemePolaska.setValue(new Date());
        formPanel.add(spinnerVrijemePolaska);

        JLabel lblVrijemeDolaska = UIStil.urediEtiketu("Vrijeme dolaska:");
        lblVrijemeDolaska.setBounds(320, 62, 120, 20);
        formPanel.add(lblVrijemeDolaska);
        spinnerVrijemeDolaska = new JSpinner(new SpinnerDateModel());
        spinnerVrijemeDolaska.setBounds(448, 62, 170, 26);
        spinnerVrijemeDolaska.setEditor(new JSpinner.DateEditor(spinnerVrijemeDolaska, "dd.MM.yyyy HH:mm"));
        spinnerVrijemeDolaska.setValue(new Date());
        formPanel.add(spinnerVrijemeDolaska);

        JLabel lblBrojSjedala = UIStil.urediEtiketu("Broj sjedala:");
        lblBrojSjedala.setBounds(320, 96, 120, 20);
        formPanel.add(lblBrojSjedala);
        txtBrojSjedala = new JTextField();
        txtBrojSjedala.setBounds(448, 96, 170, 26);
        UIStil.urediPolje(txtBrojSjedala);
        formPanel.add(txtBrojSjedala);

        JLabel lblAvion = UIStil.urediEtiketu("Dodijeli avion:");
        lblAvion.setBounds(640, 28, 120, 20);
        formPanel.add(lblAvion);
        cbAvioni = new JComboBox<>();
        cbAvioni.setBounds(770, 28, 180, 26);
        UIStil.urediComboBox(cbAvioni);
        formPanel.add(cbAvioni);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        buttonPanel.setBackground(UIStil.BOJA_POZADINE);

        JButton btnDodaj = UIStil.napraviGumb("+ Dodaj let");
        btnDodaj.addActionListener(e -> dodajLet());
        buttonPanel.add(btnDodaj);

        JButton btnAzuriraj = UIStil.napraviGumb("Ažuriraj let");
        btnAzuriraj.addActionListener(e -> azurirajLet());
        btnAzuriraj.setIcon(UIStil.ucitajIkonuSkaliranu("edit.png",24,24));
        buttonPanel.add(btnAzuriraj);

        JButton btnObrisi = UIStil.napraviNaglaseniGumb("Obriši let");
        btnObrisi.addActionListener(e -> obrisiLet());
        btnObrisi.setIcon(UIStil.ucitajIkonuSkaliranu("close.png",24,24));
        buttonPanel.add(btnObrisi);

        JButton btnOcisti = UIStil.napraviSekundarniGumb("Očisti formu");
        btnOcisti.addActionListener(e -> ocistiFormu());
        btnOcisti.setIcon(UIStil.ucitajIkonuSkaliranu("refresh.png",24,24));
        buttonPanel.add(btnOcisti);

        JPanel centarPanel = new JPanel(new BorderLayout(0, 10));
        centarPanel.setBackground(UIStil.BOJA_POZADINE);
        centarPanel.add(scrollPane, BorderLayout.CENTER);
        centarPanel.add(formPanel, BorderLayout.SOUTH);
        panel.add(centarPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        osvjeziDropdownAviona();
        ucitajLetove();

        return panel;
    }

    private void osvjeziDropdownAviona() {
        cbAvioni.removeAllItems();
        List<Avion> avioni = Avion.ucitajSveAvione();
        for (Avion a : avioni) {
            cbAvioni.addItem(a);
        }
    }

    private void popuniPoljaOdabranogReda() {
        int odabraniRed = tablicaLeta.getSelectedRow();
        if (odabraniRed >= 0) {
            txtBrojLeta.setText(tablicaLeta.getValueAt(odabraniRed, 1).toString());
            txtPolazniGrad.setText(tablicaLeta.getValueAt(odabraniRed, 2).toString());
            txtOdredisniGrad.setText(tablicaLeta.getValueAt(odabraniRed, 3).toString());

            spinnerVrijemePolaska.setValue((Timestamp) tablicaLeta.getValueAt(odabraniRed, 4));
            spinnerVrijemeDolaska.setValue((Timestamp) tablicaLeta.getValueAt(odabraniRed, 5));
            txtBrojSjedala.setText(tablicaLeta.getValueAt(odabraniRed, 6).toString());

            int idAvionaIzTablice = (int) tablicaLeta.getValueAt(odabraniRed, 7);
            for (int i = 0; i < cbAvioni.getItemCount(); i++) {
                Avion a = cbAvioni.getItemAt(i);
                if (a.getIdAviona() == idAvionaIzTablice) {
                    cbAvioni.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void ocistiFormu() {
        txtBrojLeta.setText("");
        txtPolazniGrad.setText("");
        txtOdredisniGrad.setText("");
        spinnerVrijemePolaska.setValue(new Date());
        spinnerVrijemeDolaska.setValue(new Date());
        txtBrojSjedala.setText("");
        if (cbAvioni.getItemCount() > 0) cbAvioni.setSelectedIndex(0);
        tablicaLeta.clearSelection();
    }

    private void ucitajLetove() {
        List<Let> letovi = Let.dohvatiSveLetoves();

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Broj leta", "Polazni grad", "Odredišni grad", "Vrijeme polaska", "Vrijeme dolaska", "Broj sjedala", "Avion_ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Let let : letovi) {
            model.addRow(new Object[]{
                    let.getIdLeta(),
                    let.getBrojLeta(),
                    let.getGradPolaska(),
                    let.getGradDolaska(),
                    let.getVrijemePolaska(),
                    let.getVrijemeDolaska(),
                    let.getDostupnaMjesta(),
                    let.getIdAviona()
            });
        }

        tablicaLeta.setModel(model);
        UIStil.urediTablicu(tablicaLeta);
        tablicaLeta.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablicaLeta.getColumnModel().getColumn(7).setMinWidth(0);
        tablicaLeta.getColumnModel().getColumn(7).setMaxWidth(0);
        tablicaLeta.getColumnModel().getColumn(7).setWidth(0);

        tablicaLeta.revalidate();
        tablicaLeta.repaint();
    }

    private void dodajLet() {
        if (!validirajPoljaLeta()) return;

        Avion odabraniAvion = (Avion) cbAvioni.getSelectedItem();
        if (odabraniAvion == null) {
            JOptionPane.showMessageDialog(okvir, "Morate kreirati i odabrati avion prije unosa leta!", "Greška", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection veza = uspostaviVezu()) {

            try (PreparedStatement provjera = veza.prepareStatement(
                    "SELECT COUNT(*) FROM REZERVACIJE_LETOVA WHERE let_id=?")) {

                if (tablicaLeta.getSelectedRow() >= 0) {
                    provjera.setInt(1, (int) tablicaLeta.getValueAt(tablicaLeta.getSelectedRow(), 0));

                    ResultSet rs = provjera.executeQuery();
                    if (rs.next()) {
                        int rezervirano = rs.getInt(1);
                        int brojSjedala = Integer.parseInt(txtBrojSjedala.getText().trim());

                        if (brojSjedala < rezervirano) {
                            JOptionPane.showMessageDialog(okvir,
                                    "Broj sjedala ne može biti manji od broja postojećih rezervacija (" + rezervirano + ").",
                                    "Greška",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            }

            String upit = "INSERT INTO LETOVI (broj_leta, grad_polaska, grad_dolaska, " +
                    "vrijeme_polaska, vrijeme_dolaska, dostupna_sjedista, id_avion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            Date datumPolaska = (Date) spinnerVrijemePolaska.getValue();
            Date datumDolaska = (Date) spinnerVrijemeDolaska.getValue();

            PreparedStatement izjava = veza.prepareStatement(upit);
            izjava.setString(1, txtBrojLeta.getText().trim());
            izjava.setString(2, txtPolazniGrad.getText().trim());
            izjava.setString(3, txtOdredisniGrad.getText().trim());
            izjava.setTimestamp(4, new Timestamp(datumPolaska.getTime()));
            izjava.setTimestamp(5, new Timestamp(datumDolaska.getTime()));
            izjava.setInt(6, Integer.parseInt(txtBrojSjedala.getText().trim()));
            izjava.setInt(7, odabraniAvion.getIdAviona());

            int obradjeniRedovi = izjava.executeUpdate();
            if (obradjeniRedovi > 0) {
                JOptionPane.showMessageDialog(okvir, "Let uspješno dodan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                ucitajLetove();
                ocistiFormu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri dodavanju leta: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void azurirajLet() {
        int odabraniRed = tablicaLeta.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite let za ažuriranje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validirajPoljaLeta()) return;

        Avion odabraniAvion = (Avion) cbAvioni.getSelectedItem();
        if (odabraniAvion == null) {
            JOptionPane.showMessageDialog(okvir, "Odaberite ispravan avion!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idLeta = (int) tablicaLeta.getValueAt(odabraniRed, 0);

        try (Connection veza = uspostaviVezu()) {


            try (PreparedStatement provjera = veza.prepareStatement(
                    "SELECT COUNT(*) FROM REZERVACIJE_LETOVA WHERE let_id=?")) {

                if (tablicaLeta.getSelectedRow() >= 0) {
                    provjera.setInt(1, (int) tablicaLeta.getValueAt(tablicaLeta.getSelectedRow(), 0));

                    ResultSet rs = provjera.executeQuery();
                    if (rs.next()) {
                        int rezervirano = rs.getInt(1);
                        int brojSjedala = Integer.parseInt(txtBrojSjedala.getText().trim());

                        if (brojSjedala < rezervirano) {
                            JOptionPane.showMessageDialog(okvir,
                                    "Broj sjedala ne može biti manji od broja postojećih rezervacija (" + rezervirano + ").",
                                    "Greška",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                }
            }

            String upit = "UPDATE LETOVI SET broj_leta=?, grad_polaska=?, grad_dolaska=?, " +
                    "vrijeme_polaska=?, vrijeme_dolaska=?, dostupna_sjedista=?, id_avion=? WHERE let_id=?";

            Date datumPolaska = (Date) spinnerVrijemePolaska.getValue();
            Date datumDolaska = (Date) spinnerVrijemeDolaska.getValue();

            PreparedStatement izjava = veza.prepareStatement(upit);
            izjava.setString(1, txtBrojLeta.getText().trim());
            izjava.setString(2, txtPolazniGrad.getText().trim());
            izjava.setString(3, txtOdredisniGrad.getText().trim());
            izjava.setTimestamp(4, new Timestamp(datumPolaska.getTime()));
            izjava.setTimestamp(5, new Timestamp(datumDolaska.getTime()));
            izjava.setInt(6, Integer.parseInt(txtBrojSjedala.getText().trim()));
            izjava.setInt(7, odabraniAvion.getIdAviona());
            izjava.setInt(8, idLeta);

            int obradjeniRedovi = izjava.executeUpdate();
            if (obradjeniRedovi > 0) {
                JOptionPane.showMessageDialog(okvir, "Let uspješno ažuriran!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                ucitajLetove();
                ocistiFormu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri ažuriranju leta: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisiLet() {
        int odabraniRed = tablicaLeta.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite let za brisanje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int potvrda = JOptionPane.showConfirmDialog(okvir,
                "Jeste li sigurni da želite obrisati ovaj let?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
        if (potvrda != JOptionPane.YES_OPTION) return;

        int idLeta = (int) tablicaLeta.getValueAt(odabraniRed, 0);

        try (Connection veza = uspostaviVezu()) {
            String upitBrisanjaRezervacija = "DELETE FROM REZERVACIJE_LETOVA WHERE let_id=?";
            PreparedStatement izjava1 = veza.prepareStatement(upitBrisanjaRezervacija);
            izjava1.setInt(1, idLeta);
            izjava1.executeUpdate();

            String upitBrisanjaLeta = "DELETE FROM LETOVI WHERE let_id=?";
            PreparedStatement izjava2 = veza.prepareStatement(upitBrisanjaLeta);
            izjava2.setInt(1, idLeta);

            int obradjeniRedovi = izjava2.executeUpdate();
            if (obradjeniRedovi > 0) {
                JOptionPane.showMessageDialog(okvir, "Let uspješno obrisan!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                ucitajLetove();
                ocistiFormu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri brisanju leta: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validirajPoljaLeta() {
        if (txtBrojLeta.getText().trim().isEmpty() ||
                txtPolazniGrad.getText().trim().isEmpty() ||
                txtOdredisniGrad.getText().trim().isEmpty() ||
                txtBrojSjedala.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Sva polja su obavezna", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int brojSjedala;
        try {
            brojSjedala = Integer.parseInt(txtBrojSjedala.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(okvir, "Broj sjedala mora biti broj", "Greška validacije", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Avion odabraniAvion = (Avion) cbAvioni.getSelectedItem();
        if (odabraniAvion != null && brojSjedala > odabraniAvion.getKapacitetAviona()) {
            JOptionPane.showMessageDialog(okvir,
                    "Broj sjedala ne može biti veći od kapaciteta aviona (" + odabraniAvion.getKapacitetAviona() + ").",
                    "Greška validacije",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Date datumPolaska = (Date) spinnerVrijemePolaska.getValue();
        Date datumDolaska = (Date) spinnerVrijemeDolaska.getValue();
        Date trenutnoVrijeme = new Date();

        if (datumPolaska.before(trenutnoVrijeme)) {
            JOptionPane.showMessageDialog(okvir,
                    "Vrijeme polaska ne može biti u prošlosti.",
                    "Greška validacije",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!datumDolaska.after(datumPolaska)) {
            JOptionPane.showMessageDialog(okvir,
                    "Vrijeme dolaska mora biti nakon vremena polaska.",
                    "Greška validacije",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
}