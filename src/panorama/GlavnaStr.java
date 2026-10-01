package panorama;

import java.awt.*;
import javax.swing.*;

import static model.Korisnik.hesirajPostojeceLozinke;

public class GlavnaStr {

    public JFrame okvir;

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                GlavnaStr prozor = new GlavnaStr();
                prozor.okvir.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GlavnaStr() {
        inicijaliziraj();
    }

    private void inicijaliziraj() {
        okvir = new JFrame();
        UIStil.urediokvir(okvir, "Croatia Air Trips", 540, 480);
        okvir.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okvir.getContentPane().setLayout(new BorderLayout());

        JPanel panelZaglavlja = new JPanel(new BorderLayout());
        panelZaglavlja.setBackground(UIStil.BOJA_ZAGLAVLJA_TAB);
        panelZaglavlja.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPanel naslovPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        naslovPanel.setOpaque(false);
        ImageIcon ikonaKuca = UIStil.ucitajIkonuSkaliranu("home (2).png", 36, 36);
        if (ikonaKuca != null) {
            JLabel lblIkona = new JLabel(ikonaKuca);
            naslovPanel.add(lblIkona);
        }
        JLabel lblNaslov = UIStil.urediNaslovnuEtiketu("Croatia Air Trips");
        lblNaslov.setFont(new Font("Segoe UI", Font.BOLD, 28));
        naslovPanel.add(lblNaslov);
        panelZaglavlja.add(naslovPanel, BorderLayout.CENTER);

        JLabel lblPodnaslov = new JLabel("Sustav upravljanja letovima", SwingConstants.CENTER);
        lblPodnaslov.setFont(UIStil.FONT_TEKST);
        lblPodnaslov.setForeground(UIStil.BOJA_TEKSTA_SLABIJA);
        lblPodnaslov.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        panelZaglavlja.add(lblPodnaslov, BorderLayout.SOUTH);

        okvir.add(panelZaglavlja, BorderLayout.NORTH);

        JLabel lblDobrodoslica = new JLabel("Dobrodošli! Molimo odaberite vrstu prijave.", SwingConstants.CENTER);
        lblDobrodoslica.setFont(UIStil.FONT_TEKST);
        lblDobrodoslica.setForeground(UIStil.BOJA_TEKSTA_SLABIJA);
        lblDobrodoslica.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel centralniPanel = new JPanel(new BorderLayout(0, 0));
        centralniPanel.setBackground(UIStil.BOJA_POZADINE);
        centralniPanel.add(lblDobrodoslica, BorderLayout.NORTH);

        JPanel panelGumba = new JPanel(new GridLayout(1, 2, 24, 0));
        panelGumba.setBackground(UIStil.BOJA_POZADINE);
        panelGumba.setBorder(BorderFactory.createEmptyBorder(24, 40, 40, 40));

        JPanel karticeKorisnik = napraviKarticuPrijave(
                "user.png", "Korisnik", "Prijava korisnika", "Pregled letova i rezervacije");
        JPanel karticeAdmin = napraviKarticuPrijave(
                "user-avatar.png", "Administrator", "Prijava administratora", "Upravljanje sustavom");

        JButton btnPrijavaKorisnika = (JButton) ((JPanel) karticeKorisnik.getComponent(2)).getComponent(0);
        btnPrijavaKorisnika.addActionListener(e -> {
            PrijavaKorisnika prozorPrijave = new PrijavaKorisnika();
            prozorPrijave.okvir.setVisible(true);
            okvir.dispose();
        });

        JButton btnPrijavaAdministratora = (JButton) ((JPanel) karticeAdmin.getComponent(2)).getComponent(0);
        btnPrijavaAdministratora.addActionListener(e -> {
            PrijavaAdministratora prozorPrijave = new PrijavaAdministratora();
            prozorPrijave.okvir.setVisible(true);
            okvir.dispose();
        });

        panelGumba.add(karticeKorisnik);
        panelGumba.add(karticeAdmin);
        centralniPanel.add(panelGumba, BorderLayout.CENTER);
        okvir.add(centralniPanel, BorderLayout.CENTER);
    }

    private JPanel napraviKarticuPrijave(String imeIkone, String uloga, String tekst, String opis) {
        JPanel kartica = new JPanel(new BorderLayout(0, 12));
        kartica.setBackground(UIStil.BOJA_PANELA);
        kartica.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStil.BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(24, 20, 20, 20)));

        JLabel lblIkona = new JLabel("", SwingConstants.CENTER);
        ImageIcon ikona = UIStil.ucitajIkonuSkaliranu(imeIkone, 48, 48);
        if (ikona != null) lblIkona.setIcon(ikona);
        kartica.add(lblIkona, BorderLayout.NORTH);

        JPanel tekstPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        tekstPanel.setOpaque(false);
        JLabel lblUloga = new JLabel(uloga, SwingConstants.CENTER);
        lblUloga.setFont(UIStil.FONT_PODNASLOV);
        lblUloga.setForeground(UIStil.BOJA_TEKSTA);
        JLabel lblOpis = new JLabel(opis, SwingConstants.CENTER);
        lblOpis.setFont(UIStil.FONT_TEKST);
        lblOpis.setForeground(UIStil.BOJA_TEKSTA_SLABIJA);
        tekstPanel.add(lblUloga);
        tekstPanel.add(lblOpis);
        kartica.add(tekstPanel, BorderLayout.CENTER);

        JPanel panelGumba = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelGumba.setOpaque(false);
        JButton gumb = UIStil.napraviGumb(tekst);
        gumb.setPreferredSize(new Dimension(170, 38));
        panelGumba.add(gumb);
        kartica.add(panelGumba, BorderLayout.SOUTH);

        return kartica;
    }
}