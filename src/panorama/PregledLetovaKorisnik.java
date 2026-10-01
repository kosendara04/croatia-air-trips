package panorama;

import model.Let;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PregledLetovaKorisnik {

    JFrame okvir;
    private JTable tablicaLetova;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PregledLetovaKorisnik prozor = new PregledLetovaKorisnik();
                prozor.okvir.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PregledLetovaKorisnik() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips – Pregled letova", 850, 500);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());
        okvir.getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(UIStil.BOJA_POZADINE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel naslovBar = new JPanel(new BorderLayout());
        naslovBar.setOpaque(false);
        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Pregled letova");
        lblNaslov.setFont(UIStil.FONT_PODNASLOV);
        naslovBar.add(lblNaslov, BorderLayout.WEST);
        JButton btnUcitaj = UIStil.napraviGumb("Ucitaj letove");
        btnUcitaj.setIcon(UIStil.ucitajIkonuSkaliranu("refresh.png",24,24));
        btnUcitaj.setPreferredSize(new Dimension(150, 34));
        btnUcitaj.addActionListener(e -> ucitajLetoveUTablicu());
        naslovBar.add(btnUcitaj, BorderLayout.EAST);
        panel.add(naslovBar, BorderLayout.NORTH);

        tablicaLetova = new JTable();
        tablicaLetova.setModel(new DefaultTableModel(
            new Object[][]{},
                new String[]{
                        "Broj leta",
                        "Polazni grad",
                        "Odredišni grad",
                        "Vrijeme polaska",
                        "Vrijeme dolaska",
                        "Slobodna mjesta",
                        "Model aviona"
                }
        ));
        UIStil.urediTablicu(tablicaLetova);
        JScrollPane scrollPanel = new JScrollPane(tablicaLetova);
        UIStil.urediScrollPanel(scrollPanel);
        panel.add(scrollPanel, BorderLayout.CENTER);

        ucitajLetoveUTablicu();
        return panel;
    }

    private void ucitajLetoveUTablicu() {

        DefaultTableModel model = (DefaultTableModel) tablicaLetova.getModel();
        model.setRowCount(0);

        List<Let> letovi = Let.dohvatiSveLetoveKorsinik();

        for (Let let : letovi) {
            model.addRow(new Object[]{
                    let.getBrojLeta(),
                    let.getGradPolaska(),
                    let.getGradDolaska(),
                    let.getVrijemePolaska(),
                    let.getVrijemeDolaska(),
                    let.getDostupnaMjesta(),
                    let.getModelAviona()
            });
        }
    }
}