package panorama;

import model.Avion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static model.BazaPodataka.uspostaviVezu;

public class UpravljanjeAvionimaAdmin {
    public JFrame okvir;
    private JTable tablicaAviona;
    private JTextField txtProizvodac, txtModel, txtKapacitet;

    public UpravljanjeAvionimaAdmin() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Upravljanje avionima", 900, 560);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Upravljanje avionima");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        panel.add(lblNaslov, BorderLayout.NORTH);

        tablicaAviona = new JTable();
        tablicaAviona.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablicaAviona.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                popuniPoljaOdabranogReda();
            }
        });
        UIStil.urediTablicu(tablicaAviona);
        JScrollPane scrollPane = new JScrollPane(tablicaAviona);
        UIStil.urediScrollPanel(scrollPane);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(UIStil.BOJA_PANELA);
        formPanel.setBorder(UIStil.napraviNaslovniOkvir("Detalji aviona"));
        formPanel.setPreferredSize(new Dimension(0, 130));

        JLabel lblProizvodac = UIStil.urediEtiketu("Proizvođač:");
        lblProizvodac.setBounds(16, 28, 110, 20);
        formPanel.add(lblProizvodac);
        txtProizvodac = new JTextField();
        txtProizvodac.setBounds(132, 28, 220, 26);
        UIStil.urediPolje(txtProizvodac);
        formPanel.add(txtProizvodac);

        JLabel lblModel = UIStil.urediEtiketu("Model aviona:");
        lblModel.setBounds(16, 62, 110, 20);
        formPanel.add(lblModel);
        txtModel = new JTextField();
        txtModel.setBounds(132, 62, 220, 26);
        UIStil.urediPolje(txtModel);
        formPanel.add(txtModel);

        JLabel lblKapacitet = UIStil.urediEtiketu("Kapacitet sjedala:");
        lblKapacitet.setBounds(390, 28, 130, 20);
        formPanel.add(lblKapacitet);
        txtKapacitet = new JTextField();
        txtKapacitet.setBounds(530, 28, 160, 26);
        UIStil.urediPolje(txtKapacitet);
        formPanel.add(txtKapacitet);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        buttonPanel.setBackground(UIStil.BOJA_POZADINE);

        JButton btnDodaj = UIStil.napraviGumb("+ Dodaj avion");
        btnDodaj.addActionListener(e -> dodajAvion());
        buttonPanel.add(btnDodaj);

        JButton btnAzuriraj = UIStil.napraviGumb("Azuriraj avion");
        btnAzuriraj.addActionListener(e -> azurirajAvion());
        btnAzuriraj.setIcon(UIStil.ucitajIkonuSkaliranu("edit.png",24,24));
        buttonPanel.add(btnAzuriraj);

        JButton btnObrisi = UIStil.napraviNaglaseniGumb("Obrisi avion");
        btnObrisi.addActionListener(e -> obrisiAvion());
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

        ucitajAvione();
        return panel;
    }

    private void popuniPoljaOdabranogReda() {
        int odabraniRed = tablicaAviona.getSelectedRow();
        if (odabraniRed >= 0) {
            txtProizvodac.setText(tablicaAviona.getValueAt(odabraniRed, 1).toString());
            txtModel.setText(tablicaAviona.getValueAt(odabraniRed, 2).toString());
            txtKapacitet.setText(tablicaAviona.getValueAt(odabraniRed, 3).toString());
        }
    }

    private void ocistiFormu() {
        txtProizvodac.setText("");
        txtModel.setText("");
        txtKapacitet.setText("");
        tablicaAviona.clearSelection();
    }

    private void ucitajAvione() {
        List<Avion> avioni = Avion.ucitajSveAvione();
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Proizvođač", "Model", "Kapacitet"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Avion a : avioni) {
            model.addRow(new Object[]{a.getIdAviona(), a.getProizvodacAviona(), a.getModelAviona(), a.getKapacitetAviona()});
        }
        tablicaAviona.setModel(model);
        UIStil.urediTablicu(tablicaAviona);
        tablicaAviona.getColumnModel().getColumn(0).setPreferredWidth(50);
    }

    private void dodajAvion() {
        if (!validirajPolja()) return;
        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement(
                     "INSERT INTO AVION (proizvodac_aviona, model_aviona, kapacitet_aviona) VALUES (?, ?, ?)")) {
            stmt.setString(1, txtProizvodac.getText().trim());
            stmt.setString(2, txtModel.getText().trim());
            stmt.setInt(3, Integer.parseInt(txtKapacitet.getText().trim()));

            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(okvir, "Avion uspješno dodan!");
                ucitajAvione();
                ocistiFormu();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(okvir, "Greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void azurirajAvion() {
        int odabraniRed = tablicaAviona.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite avion za ažuriranje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validirajPolja()) return;
        int idAviona = (int) tablicaAviona.getValueAt(odabraniRed, 0);

        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement(
                     "UPDATE AVION SET proizvodac_aviona=?, model_aviona=?, kapacitet_aviona=? WHERE id_aviona=?")) {
            stmt.setString(1, txtProizvodac.getText().trim());
            stmt.setString(2, txtModel.getText().trim());
            stmt.setInt(3, Integer.parseInt(txtKapacitet.getText().trim()));
            stmt.setInt(4, idAviona);

            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(okvir, "Avion uspješno ažuriran!");
                ucitajAvione();
                ocistiFormu();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(okvir, "Greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obrisiAvion() {
        int odabraniRed = tablicaAviona.getSelectedRow();
        if (odabraniRed < 0) {
            JOptionPane.showMessageDialog(okvir, "Odaberite avion za brisanje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int potvrda = JOptionPane.showConfirmDialog(okvir, "Želite li obrisati avion? Povezani letovi mogu izgubiti referencu.", "Potvrda", JOptionPane.YES_NO_OPTION);
        if (potvrda != JOptionPane.YES_OPTION) return;

        int idAviona = (int) tablicaAviona.getValueAt(odabraniRed, 0);
        try (Connection veza = uspostaviVezu();
             PreparedStatement stmt = veza.prepareStatement("DELETE FROM AVION WHERE id_aviona=?")) {
            stmt.setInt(1, idAviona);
            if (stmt.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(okvir, "Avion uspješno obrisan!");
                ucitajAvione();
                ocistiFormu();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(okvir, "Greška pri brisanju: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validirajPolja() {
        if (txtProizvodac.getText().trim().isEmpty() || txtModel.getText().trim().isEmpty() || txtKapacitet.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(okvir, "Sva polja su obavezna!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(txtKapacitet.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(okvir, "Kapacitet mora biti broj!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}