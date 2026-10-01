package panorama;

import model.Izvjestaj;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static model.BazaPodataka.uspostaviVezu;

public class PregledIzvjestaja {

    private JFrame okvir;
    private JTabbedPane tabbedPane;
    private JTable tablicaRezervacija, tablicaPutnika, tablicaLetova, tablicaAviona;
    private JButton btnOdjava, btnIzvoz, btnOsvjezi;

    public PregledIzvjestaja() {
        initialize();
    }

    private void initialize() {
        okvir = new JFrame("Sustav Izvještavanja");
        UIStil.urediokvir(okvir, "Sustav Izvještavanja", 1000, 580);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);

        okvir.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                generirajSveIzvjestaje();
            }
        });
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel naslovBar = new JPanel(new BorderLayout());
        naslovBar.setOpaque(false);
        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Sustav za izradu izvještaja");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        naslovBar.add(lblNaslov, BorderLayout.WEST);

        ImageIcon ikonaIzvjestaj = UIStil.ucitajIkonuSkaliranu("report.png", 22, 22);
        ImageIcon ikonaNota = UIStil.ucitajIkonuSkaliranu("note.png", 22, 22);
        ImageIcon ikonaKlipbord = UIStil.ucitajIkonuSkaliranu("clipboard.png", 22, 22);
        ImageIcon ikonaRezervacija = UIStil.ucitajIkonuSkaliranu("reservation.png", 22, 22);

        panel.add(naslovBar, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        UIStil.urediTabbedPane(tabbedPane);

        tablicaRezervacija = new JTable();
        UIStil.urediTablicu(tablicaRezervacija);
        JScrollPane spRezervacije = new JScrollPane(tablicaRezervacija);
        UIStil.urediScrollPanel(spRezervacije);
        tabbedPane.addTab("Izvještaj o rezervacijama", ikonaRezervacija, spRezervacije, "Pregled svih rezervacija i putnika");

        tablicaPutnika = new JTable();
        UIStil.urediTablicu(tablicaPutnika);
        JScrollPane spPutnici = new JScrollPane(tablicaPutnika);
        UIStil.urediScrollPanel(spPutnici);
        tabbedPane.addTab("Izvještaj o putnicima", ikonaKlipbord, spPutnici, "Registar registriranih putnika u bazi");

        tablicaLetova = new JTable();
        UIStil.urediTablicu(tablicaLetova);
        JScrollPane spLetovi = new JScrollPane(tablicaLetova);
        UIStil.urediScrollPanel(spLetovi);
        tabbedPane.addTab("Izvještaj o letovima", ikonaNota, spLetovi, "Statistika operativnih letova");

        tablicaAviona = new JTable();
        UIStil.urediTablicu(tablicaAviona);
        JScrollPane spAvioni = new JScrollPane(tablicaAviona);
        UIStil.urediScrollPanel(spAvioni);
        tabbedPane.addTab("Izvještaj o avionima", ikonaIzvjestaj, spAvioni, "Pregled flote i kapaciteta");

        panel.add(tabbedPane, BorderLayout.CENTER);

        btnOdjava = UIStil.napraviSekundarniGumb("← Povratak na izbornik");
        btnOsvjezi = UIStil.napraviSekundarniGumb("Osvježi");
        btnOsvjezi.addActionListener(e -> generirajSveIzvjestaje());
        btnIzvoz = UIStil.napraviGumb("Izvoz izvještaja");
        btnIzvoz.addActionListener(e -> izvezi());

        JPanel dnoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dnoPanel.setBackground(UIStil.BOJA_POZADINE);
//        dnoPanel.add(btnOdjava);
        dnoPanel.add(btnOsvjezi);
        dnoPanel.add(btnIzvoz);
        panel.add(dnoPanel, BorderLayout.SOUTH);

        generirajSveIzvjestaje();
        return panel;
    }

    private void generirajSveIzvjestaje() {
        try (Connection veza = uspostaviVezu()) {
            DefaultTableModel modRez = new DefaultTableModel(
                    new Object[]{
                            "ID Rezervacije",
                            "Putnik",
                            "Broj Leta",
                            "Grad Polaska",
                            "Grad Dolaska",
                            "Vrijeme Polaska",
                            "Vrijeme Dolaska",
                            "Rezervirano",
                            "Ukupno Rezervirano",
                            "Dostupna Sjedala",
                            "Avion",
                            "Datum Rezervacije"
                    }, 0);

            String q1 =
                    "SELECT " +
                            "r.rezervacija_id, " +
                            "k.ime, " +
                            "l.broj_leta, " +
                            "l.grad_polaska, " +
                            "l.grad_dolaska, " +
                            "l.vrijeme_polaska, " +
                            "l.vrijeme_dolaska, " +
                            "r.broj_sjedista AS rezervirano, " +
                            "COALESCE(b.ukupno_rezervirano, 0) AS ukupno_rezervirano, " +
                            "l.dostupna_sjedista, " +
                            "a.model_aviona, " +
                            "r.datum_rezervacije " +
                            "FROM REZERVACIJE_LETOVA r " +
                            "JOIN KORISNIK k ON r.korisnik_id = k.id " +
                            "JOIN LETOVI l ON r.let_id = l.let_id " +
                            "JOIN AVION a ON l.id_avion = a.id_aviona " +
                            "LEFT JOIN ( " +
                            "    SELECT let_id, SUM(broj_sjedista) AS ukupno_rezervirano " +
                            "    FROM REZERVACIJE_LETOVA " +
                            "    GROUP BY let_id " +
                            ") b ON l.let_id = b.let_id " +
                            "ORDER BY r.datum_rezervacije DESC";

            Statement s1 = veza.createStatement();
            ResultSet rs1 = s1.executeQuery(q1);

            while (rs1.next()) {
                modRez.addRow(new Object[]{
                        rs1.getInt("rezervacija_id"),
                        rs1.getString("ime"),
                        rs1.getString("broj_leta"),
                        rs1.getString("grad_polaska"),
                        rs1.getString("grad_dolaska"),
                        rs1.getTimestamp("vrijeme_polaska"),
                        rs1.getTimestamp("vrijeme_dolaska"),
                        rs1.getInt("rezervirano"),
                        rs1.getInt("ukupno_rezervirano"),
                        rs1.getInt("dostupna_sjedista"),
                        rs1.getString("model_aviona"),
                        rs1.getTimestamp("datum_rezervacije")
                });
            }

            tablicaRezervacija.setModel(modRez);

            DefaultTableModel modPut = new DefaultTableModel(new Object[]{"ID Putnika", "Ime i prezime", "E-mail adresa", "Kontakt broj", "Registriran"}, 0);
            String q2 = "SELECT id, ime, email, kontaktBroj_korisnika, kreirano FROM KORISNIK WHERE uloga = 'user'";
            Statement s2 = veza.createStatement();
            ResultSet rs2 = s2.executeQuery(q2);
            while (rs2.next()) {
                modPut.addRow(new Object[]{rs2.getInt(1), rs2.getString(2), rs2.getString(3), rs2.getString(4), rs2.getTimestamp(5)});
            }
            tablicaPutnika.setModel(modPut);

            DefaultTableModel modLet = new DefaultTableModel(new Object[]{"Broj leta", "Polazak", "Dolazak", "Vrijeme Polaska", "Preostala Sjedala"}, 0);
            String q3 = "SELECT broj_leta, grad_polaska, grad_dolaska, vrijeme_polaska, dostupna_sjedista FROM LETOVI";
            Statement s3 = veza.createStatement();
            ResultSet rs3 = s3.executeQuery(q3);
            while (rs3.next()) {
                modLet.addRow(new Object[]{rs3.getString(1), rs3.getString(2), rs3.getString(3), rs3.getTimestamp(4), rs3.getInt(5)});
            }
            tablicaLetova.setModel(modLet);

            DefaultTableModel modAv = new DefaultTableModel(new Object[]{"ID", "Proizvođač", "Model", "Kapacitet sjedala"}, 0);
            String q4 = "SELECT id_aviona, proizvodac_aviona, model_aviona, kapacitet_aviona FROM AVION";
            Statement s4 = veza.createStatement();
            ResultSet rs4 = s4.executeQuery(q4);
            while (rs4.next()) {
                modAv.addRow(new Object[]{rs4.getInt(1), rs4.getString(2), rs4.getString(3), rs4.getInt(4)});
            }
            tablicaAviona.setModel(modAv);

        } catch (SQLException e) {
            System.out.println("Greška prilikom generiranja matrica izvještaja: " + e.getMessage());
        }
    }

    private JTable dohvatiAktivnuTablicu() {
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                return tablicaRezervacija;
            case 1:
                return tablicaPutnika;
            case 2:
                return tablicaLetova;
            case 3:
                return tablicaAviona;
            default:
                return tablicaRezervacija;
        }
    }

    private String dohvatiNazivAktivnogIzvjestaja() {
        switch (tabbedPane.getSelectedIndex()) {
            case 0:
                return "izvjestaj_rezervacije";
            case 1:
                return "izvjestaj_putnici";
            case 2:
                return "izvjestaj_letovi";
            case 3:
                return "izvjestaj_avioni";
            default:
                return "izvjestaj";
        }
    }

    private void izvezi() {
        JTable trenutnaTablica = dohvatiAktivnuTablicu();
        String nazivIzvjestaja = dohvatiNazivAktivnogIzvjestaja();

        JFileChooser odabirac = new JFileChooser();
        odabirac.setDialogTitle("Izvoz izvještaja");
        odabirac.setSelectedFile(new File(nazivIzvjestaja));
        odabirac.setAcceptAllFileFilterUsed(false);
        odabirac.addChoosableFileFilter(new FileNameExtensionFilter("CSV datoteka (*.csv)", "csv"));
        odabirac.addChoosableFileFilter(new FileNameExtensionFilter("Tekstualna datoteka (*.txt)", "txt"));
        odabirac.addChoosableFileFilter(new FileNameExtensionFilter("Excel datoteka (*.xlsx)", "xlsx"));

        int rezultat = odabirac.showSaveDialog(okvir);
        if (rezultat != JFileChooser.APPROVE_OPTION) return;

        File odabranaDatoteka = odabirac.getSelectedFile();
        FileNameExtensionFilter odabraniFilter = (FileNameExtensionFilter) odabirac.getFileFilter();
        String ekstenzija = odabraniFilter.getExtensions()[0];

        if (!odabranaDatoteka.getName().toLowerCase().endsWith("." + ekstenzija)) {
            odabranaDatoteka = new File(odabranaDatoteka.getAbsolutePath() + "." + ekstenzija);
        }

        try {
            switch (ekstenzija) {
                case "csv":
                    izvezuCsv(trenutnaTablica, odabranaDatoteka);
                    break;
                case "txt":
                    izvezuTxt(trenutnaTablica, odabranaDatoteka);
                    break;
                case "xlsx":
                    izvezuExcel(trenutnaTablica, odabranaDatoteka, nazivIzvjestaja);
                    break;
            }
            JOptionPane.showMessageDialog(okvir, "Izvještaj uspješno izvezen!", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(okvir, "Greška pri izvozu: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String csvEscape(Object vrijednost) {
        if (vrijednost == null) return "";
        String tekst = vrijednost.toString();
        if (tekst.contains(",") || tekst.contains("\"") || tekst.contains("\n")) {
            tekst = tekst.replace("\"", "\"\"");
            tekst = "\"" + tekst + "\"";
        }
        return tekst;
    }

    private void izvezuCsv(JTable tablica, File datoteka) throws IOException {
        try (PrintWriter pisac = new PrintWriter(new OutputStreamWriter(new FileOutputStream(datoteka), StandardCharsets.UTF_8))) {
            TableModel model = tablica.getModel();
            int brojKolona = model.getColumnCount();

            StringBuilder zaglavlje = new StringBuilder();
            for (int kol = 0; kol < brojKolona; kol++) {
                zaglavlje.append(csvEscape(model.getColumnName(kol)));
                if (kol < brojKolona - 1) zaglavlje.append(",");
            }
            pisac.println(zaglavlje);

            for (int red = 0; red < model.getRowCount(); red++) {
                StringBuilder redak = new StringBuilder();
                for (int kol = 0; kol < brojKolona; kol++) {
                    redak.append(csvEscape(model.getValueAt(red, kol)));
                    if (kol < brojKolona - 1) redak.append(",");
                }
                pisac.println(redak);
            }
        }
    }

    private void izvezuTxt(JTable tablica, File datoteka) throws IOException {
        try (PrintWriter pisac = new PrintWriter(new OutputStreamWriter(new FileOutputStream(datoteka), StandardCharsets.UTF_8))) {
            TableModel model = tablica.getModel();
            int brojKolona = model.getColumnCount();

            for (int kol = 0; kol < brojKolona; kol++) {
                pisac.print(model.getColumnName(kol));
                if (kol < brojKolona - 1) pisac.print("\t");
            }
            pisac.println();

            for (int red = 0; red < model.getRowCount(); red++) {
                for (int kol = 0; kol < brojKolona; kol++) {
                    Object vrijednost = model.getValueAt(red, kol);
                    pisac.print(vrijednost == null ? "" : vrijednost.toString());
                    if (kol < brojKolona - 1) pisac.print("\t");
                }
                pisac.println();
            }
        }
    }

    private void izvezuExcel(JTable tablica, File datoteka, String nazivLista) throws IOException {
        TableModel model = tablica.getModel();
        int brojKolona = model.getColumnCount();

        try (XSSFWorkbook radnaKnjiga = new XSSFWorkbook()) {
            Sheet list = radnaKnjiga.createSheet(nazivLista);
            CellStyle stilDatuma = radnaKnjiga.createCellStyle();
                stilDatuma.setDataFormat(
                radnaKnjiga.createDataFormat().getFormat("dd.mm.yyyy hh:mm")
);

            CellStyle stilZaglavlja = radnaKnjiga.createCellStyle();
            Font fontZaglavlja = radnaKnjiga.createFont();
            fontZaglavlja.setBold(true);
            stilZaglavlja.setFont(fontZaglavlja);

            Row redZaglavlja = list.createRow(0);
            for (int kol = 0; kol < brojKolona; kol++) {
                Cell celija = redZaglavlja.createCell(kol);
                celija.setCellValue(model.getColumnName(kol));
                celija.setCellStyle(stilZaglavlja);
            }

            for (int red = 0; red < model.getRowCount(); red++) {
                Row noviRed = list.createRow(red + 1);
                for (int kol = 0; kol < brojKolona; kol++) {
                    Object vrijednost = model.getValueAt(red, kol);
                    Cell celija = noviRed.createCell(kol);
                    if (vrijednost == null) {
                        celija.setCellValue("");
                    } else if (vrijednost instanceof Number) {
                        celija.setCellValue(((Number) vrijednost).doubleValue());
                    } else if (vrijednost instanceof java.util.Date) {
                        celija.setCellValue((java.util.Date) vrijednost);
                        celija.setCellStyle(stilDatuma);
                    } else {
                        celija.setCellValue(vrijednost.toString());
                    }
                }
            }

            for (int kol = 0; kol < brojKolona; kol++) {
                list.autoSizeColumn(kol);
            }

            try (FileOutputStream izlaz = new FileOutputStream(datoteka)) {
                radnaKnjiga.write(izlaz);
            }
        }
    }

    public void setReportData(List<Izvjestaj> izvjestaji) {

    }

    public JButton getBtnOdjava() {
        return btnOdjava;
    }

    public JButton getBtnIzvoz() {
        return btnIzvoz;
    }

    public JButton getBtnOsvjezi() {
        return btnOsvjezi;
    }

    public JFrame getOkvir() {
        return okvir;
    }

    public void show() {
        okvir.setVisible(true);
    }
}
