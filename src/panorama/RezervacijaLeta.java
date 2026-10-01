package panorama;

import model.Izvjestaj;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.sql.*;

import static model.BazaPodataka.uspostaviVezu;

public class RezervacijaLeta {

    JFrame okvir;
    private JComboBox<String> padajuciIzbornikLetova;
    private JSpinner spinnerSjedala;

    public RezervacijaLeta() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Rezervacija leta", 850, 500);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Rezervacija leta");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        panel.add(lblNaslov, BorderLayout.NORTH);

        JPanel kartica = new JPanel(null);
        kartica.setBackground(UIStil.BOJA_PANELA);
        kartica.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStil.BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)));
        kartica.setPreferredSize(new Dimension(600, 260));

        JLabel lblOdabirLeta = UIStil.urediEtiketu("Odaberi let:");
        lblOdabirLeta.setBounds(0, 0, 140, 18);
        kartica.add(lblOdabirLeta);

        padajuciIzbornikLetova = new JComboBox<>();
        UIStil.urediComboBox(padajuciIzbornikLetova);
        padajuciIzbornikLetova.setBounds(150, 0, 380, 30);
        padajuciIzbornikLetova.addActionListener(e -> azurirajSpinnerSjedala());
        kartica.add(padajuciIzbornikLetova);

        JLabel lblBrojSjedala = UIStil.urediEtiketu("Broj sjedala:");
        lblBrojSjedala.setBounds(0, 50, 140, 18);
        kartica.add(lblBrojSjedala);

        spinnerSjedala = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spinnerSjedala.setFont(UIStil.FONT_TEKST);
        spinnerSjedala.setBackground(UIStil.BOJA_KARTICE);
        spinnerSjedala.getEditor().getComponent(0).setBackground(UIStil.BOJA_KARTICE);
        ((JComponent) spinnerSjedala.getEditor().getComponent(0)).setForeground(UIStil.BOJA_TEKSTA);
        spinnerSjedala.setBounds(150, 48, 100, 30);
        kartica.add(spinnerSjedala);

        JButton btnRezerviraj = UIStil.napraviGumb("Rezerviraj let");
        btnRezerviraj.setBounds(0, 110, 200, 40);
        btnRezerviraj.addActionListener(e -> rezervirajLet());
        btnRezerviraj.setIcon(UIStil.ucitajIkonuSkaliranu("user-avatar.png", 22, 22));
        kartica.add(btnRezerviraj);

        JButton btnExport = UIStil.napraviSekundarniGumb("↓ Izvezi rezervacije");
        btnExport.setBounds(210, 110, 200, 40);
        btnExport.addActionListener(e -> exportReservationsToExcel());
//        kartica.add(btnExport);

        ucitajLetove();

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapperPanel.setBackground(UIStil.BOJA_POZADINE);
        wrapperPanel.add(kartica);
        panel.add(wrapperPanel, BorderLayout.CENTER);

        return panel;
    }

    private void ucitajLetove() {

        try (Connection veza = uspostaviVezu()) {

            String upit = "SELECT broj_leta, grad_polaska, grad_dolaska FROM LETOVI";

            PreparedStatement izjava = veza.prepareStatement(upit);
            ResultSet rezultati = izjava.executeQuery();

            padajuciIzbornikLetova.removeAllItems();

            while (rezultati.next()) {
                String let = rezultati.getString("broj_leta")
                        + " - " +
                        rezultati.getString("grad_polaska")
                        + " → " +
                        rezultati.getString("grad_dolaska");

                padajuciIzbornikLetova.addItem(let);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri učitavanju letova: " + e.getMessage());
        }
    }

    private void azurirajSpinnerSjedala() {
        SesijaKorisnika sesija = SesijaKorisnika.getInstance();
        int idKorisnika = sesija.getIdKorisnika();
        String odabrano = (String) padajuciIzbornikLetova.getSelectedItem();

        if (odabrano == null || !odabrano.contains("(Već rezervirano)")) {
            spinnerSjedala.setValue(1);
            return;
        }

        try (Connection veza = uspostaviVezu()) {
            String brojLeta = odabrano.split(" - ")[0];
            String upit = "SELECT r.broj_sjedista, l.dostupna_sjedista " +
                    "FROM REZERVACIJE_LETOVA r " +
                    "JOIN LETOVI l ON r.let_id = l.let_id " +
                    "WHERE r.korisnik_id = ? AND l.broj_leta = ?";

            PreparedStatement izjava = veza.prepareStatement(upit);
            izjava.setInt(1, idKorisnika);
            izjava.setString(2, brojLeta);
            ResultSet rezultati = izjava.executeQuery();

            if (rezultati.next()) {
                int trenutnaSjedala = rezultati.getInt("broj_sjedista");
                int dostupnaSjedala = rezultati.getInt("dostupna_sjedista");
                spinnerSjedala.setModel(new SpinnerNumberModel(
                        trenutnaSjedala, 1, trenutnaSjedala + dostupnaSjedala, 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void rezervirajLet() {
        SesijaKorisnika sesija = SesijaKorisnika.getInstance();
        int idKorisnika = sesija.getIdKorisnika();

        if (idKorisnika == 0) {
            JOptionPane.showMessageDialog(okvir, "Molimo prijavite se prvo");
            if (okvir != null) okvir.dispose();
            new PrijavaKorisnika().okvir.setVisible(true);
            return;
        }

        if (padajuciIzbornikLetova.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(okvir, "Molimo odaberite let");
            return;
        }

        String odabraniLet = (String) padajuciIzbornikLetova.getSelectedItem();
        String brojLeta = odabraniLet.split(" - ")[0];
        int sjedala = (int) spinnerSjedala.getValue();

        try (Connection veza = uspostaviVezu()) {
            veza.setAutoCommit(false);

            String upitLeta = "SELECT let_id, dostupna_sjedista, grad_polaska, grad_dolaska, vrijeme_polaska FROM LETOVI WHERE broj_leta = ? FOR UPDATE";
            PreparedStatement izjava = veza.prepareStatement(upitLeta);
            izjava.setString(1, brojLeta);
            ResultSet rezultati = izjava.executeQuery();

            if (!rezultati.next()) {
                JOptionPane.showMessageDialog(okvir, "Let nije pronađen");
                veza.rollback();
                return;
            }

            int idLeta = rezultati.getInt("let_id");
            int dostupnaSjedala = rezultati.getInt("dostupna_sjedista");
            String gradPolaska = rezultati.getString("grad_polaska");
            String gradDolaska = rezultati.getString("grad_dolaska");
            Timestamp vrijemePolaska = rezultati.getTimestamp("vrijeme_polaska");

            String upitRezervacije = "SELECT rezervacija_id, broj_sjedista FROM REZERVACIJE_LETOVA WHERE korisnik_id = ? AND let_id = ?";
            izjava = veza.prepareStatement(upitRezervacije);
            izjava.setInt(1, idKorisnika);
            izjava.setInt(2, idLeta);
            rezultati = izjava.executeQuery();

            if (rezultati.next()) {
                int idRezervacije = rezultati.getInt("rezervacija_id");
                int trenutnaSjedala = rezultati.getInt("broj_sjedista");
                int razlikaSjedala = sjedala - trenutnaSjedala;

                if (razlikaSjedala == 0) {
                    JOptionPane.showMessageDialog(okvir, "Nema promjena za spremanje");
                    veza.rollback();
                    return;
                }

                if (razlikaSjedala > dostupnaSjedala) {
                    JOptionPane.showMessageDialog(okvir,
                            "Nema dovoljno slobodnih sjedala. Dostupno: " + dostupnaSjedala);
                    veza.rollback();
                    return;
                }

                String upitAžuriranja = "UPDATE REZERVACIJE_LETOVA SET broj_sjedista = ? WHERE rezervacija_id = ?";
                izjava = veza.prepareStatement(upitAžuriranja);
                izjava.setInt(1, sjedala);
                izjava.setInt(2, idRezervacije);
                izjava.executeUpdate();

                String upitSjedala = "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista - ? WHERE let_id = ?";
                izjava = veza.prepareStatement(upitSjedala);
                izjava.setInt(1, razlikaSjedala);
                izjava.setInt(2, idLeta);
                izjava.executeUpdate();

                JOptionPane.showMessageDialog(okvir,
                        "Rezervacija ažurirana! Promjena s " + trenutnaSjedala + " na " + sjedala + " sjedala");
            } else {
                if (sjedala > dostupnaSjedala) {
                    JOptionPane.showMessageDialog(okvir,
                            "Nema dovoljno slobodnih sjedala. Dostupno: " + dostupnaSjedala);
                    veza.rollback();
                    return;
                }

                String upitUnosa = "INSERT INTO REZERVACIJE_LETOVA (korisnik_id, let_id, broj_sjedista) VALUES (?, ?, ?)";
                izjava = veza.prepareStatement(upitUnosa);
                izjava.setInt(1, idKorisnika);
                izjava.setInt(2, idLeta);
                izjava.setInt(3, sjedala);
                izjava.executeUpdate();

                String upitSjedala = "UPDATE LETOVI SET dostupna_sjedista = dostupna_sjedista - ? WHERE let_id = ?";
                izjava = veza.prepareStatement(upitSjedala);
                izjava.setInt(1, sjedala);
                izjava.setInt(2, idLeta);
                izjava.executeUpdate();
            }

            Izvjestaj izvjestaj = new Izvjestaj(idKorisnika, gradPolaska, gradDolaska, vrijemePolaska);
            boolean reportInserted = Izvjestaj.insertIzvjestaj(izvjestaj);

            if (reportInserted) {
                System.out.println("Izvještaj uspješno dodan!");
            } else {
                System.out.println("Greška pri dodavanju izvještaja.");
            }

            JOptionPane.showMessageDialog(okvir, "Let uspješno rezerviran za " + sjedala + " sjedala!");

            veza.commit();
            ucitajLetove();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri rezervaciji: " + e.getMessage());
        }
    }


    private void exportReservationsToExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Odaberite lokaciju za spremanje");
        fileChooser.setSelectedFile(new java.io.File("rezervacije.xlsx"));

        int userSelection = fileChooser.showSaveDialog(okvir);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();

        try (Connection veza = uspostaviVezu()) {
            String query = "SELECT r.rezervacija_id, k.ime, l.broj_leta, l.grad_polaska, l.grad_dolaska, l.vrijeme_polaska, r.broj_sjedista " +
                    "FROM REZERVACIJE_LETOVA r " +
                    "JOIN KORISNIK k ON r.korisnik_id = k.id " +
                    "JOIN LETOVI l ON r.let_id = l.let_id";

            PreparedStatement ps = veza.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Rezervacije");

            String[] headers = {"ID Rezervacije", "Ime korisnika", "Broj leta", "Polazak", "Dolazak", "Vrijeme polaska", "Broj sjedala"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getInt("rezervacija_id"));
                row.createCell(1).setCellValue(rs.getString("ime"));
                row.createCell(2).setCellValue(rs.getString("broj_leta"));
                row.createCell(3).setCellValue(rs.getString("grad_polaska"));
                row.createCell(4).setCellValue(rs.getString("grad_dolaska"));
                row.createCell(5).setCellValue(rs.getTimestamp("vrijeme_polaska").toString());
                row.createCell(6).setCellValue(rs.getInt("broj_sjedista"));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            workbook.close();

            JOptionPane.showMessageDialog(okvir, "Rezervacije uspješno izvezene u " + filePath);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(okvir, "Greška pri izvozu: " + ex.getMessage());
        }
    }

}