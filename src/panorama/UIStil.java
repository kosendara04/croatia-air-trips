package panorama;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class UIStil {

    public static final Color BOJA_POZADINE       = new Color(245, 247, 250);
    public static final Color BOJA_PANELA         = Color.WHITE;
    public static final Color BOJA_KARTICE        = Color.WHITE;

    public static final Color BOJA_NAGLASKA       = new Color(25, 118, 210);
    public static final Color BOJA_NAGLASKA_HOVER = new Color(21, 101, 192);

    public static final Color BOJA_SIDEBAR        = new Color(18, 62, 120);
    public static final Color BOJA_SIDEBAR_HOVER  = new Color(25, 85, 160);

    public static final Color BOJA_OPASNOSTI      = new Color(211, 47, 47);
    public static final Color BOJA_USPJEH         = new Color(46, 125, 50);
    public static final Color BOJA_UPOZORENJE     = new Color(237, 108, 2);

    public static final Color BOJA_TEKSTA         = new Color(33, 37, 41);
    public static final Color BOJA_TEKSTA_SLABIJA = new Color(108, 117, 125);

    public static final Color BOJA_OBRUBA         = new Color(222, 226, 230);
    public static final Color BOJA_REDA_ALT       = new Color(248, 249, 251);
    public static final Color BOJA_ODABIRA        = new Color(187, 222, 251);

    public static final Color BOJA_ZAGLAVLJA_TAB   = Color.WHITE;
    public static final Color BOJA_ZAGLAVLJE_TEKST = new Color(25, 118, 210);

    public static final Font FONT_NASLOV    = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_PODNASLOV = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_TEKST     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_GUMB      = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLICA   = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_ZAGLAVLJE = new Font("Segoe UI", Font.BOLD, 12);

    public static void urediokvir(JFrame okvir, String naslov, int sirina, int visina) {
        okvir.setTitle(naslov);
        okvir.setSize(sirina, visina);
        okvir.setLocationRelativeTo(null);
        okvir.getContentPane().setBackground(BOJA_POZADINE);
    }

    public static JButton napraviGumb(String tekst) {
        JButton gumb = new JButton(tekst);
        gumb.setFont(FONT_GUMB);
        gumb.setBackground(BOJA_NAGLASKA);
        gumb.setForeground(Color.WHITE);
        gumb.setFocusPainted(false);
        gumb.setBorderPainted(false);
        gumb.setOpaque(true);
        gumb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gumb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                gumb.setBackground(BOJA_NAGLASKA_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                gumb.setBackground(BOJA_NAGLASKA);
            }
        });

        return gumb;
    }

    public static JButton napraviSekundarniGumb(String tekst) {
        JButton gumb = new JButton(tekst);
        gumb.setFont(FONT_GUMB);
        gumb.setBackground(BOJA_KARTICE);
        gumb.setForeground(BOJA_TEKSTA);
        gumb.setFocusPainted(false);
        gumb.setBorder(BorderFactory.createLineBorder(BOJA_OBRUBA, 1));
        gumb.setOpaque(true);
        gumb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gumb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                gumb.setBackground(BOJA_REDA_ALT);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                gumb.setBackground(BOJA_KARTICE);
            }
        });

        return gumb;
    }

    public static JButton napraviNaglaseniGumb(String tekst) {
        JButton gumb = new JButton(tekst);
        gumb.setFont(FONT_GUMB);
        gumb.setBackground(BOJA_OPASNOSTI);
        gumb.setForeground(Color.WHITE);
        gumb.setFocusPainted(false);
        gumb.setBorderPainted(false);
        gumb.setOpaque(true);
        gumb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return gumb;
    }

    public static void urediTablicu(JTable tablica) {
        tablica.setFont(FONT_TABLICA);
        tablica.setBackground(BOJA_PANELA);
        tablica.setForeground(BOJA_TEKSTA);
        tablica.setGridColor(BOJA_OBRUBA);
        tablica.setRowHeight(26);
        tablica.setSelectionBackground(BOJA_ODABIRA);
        tablica.setSelectionForeground(Color.BLACK);
        tablica.setShowHorizontalLines(true);
        tablica.setShowVerticalLines(false);
        tablica.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader zaglavlje = tablica.getTableHeader();
        zaglavlje.setFont(FONT_ZAGLAVLJE);
        zaglavlje.setBackground(BOJA_ZAGLAVLJA_TAB);
        zaglavlje.setForeground(BOJA_ZAGLAVLJE_TEKST);
        zaglavlje.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BOJA_NAGLASKA));

        tablica.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BOJA_PANELA : BOJA_REDA_ALT);
                    setForeground(BOJA_TEKSTA);
                }

                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                return this;
            }
        });
    }

    public static void urediScrollPanel(JScrollPane scroll) {
        scroll.getViewport().setBackground(BOJA_PANELA);
        scroll.setBorder(BorderFactory.createLineBorder(BOJA_OBRUBA, 1));
    }

    public static JTextField urediPolje(JTextField polje) {
        polje.setFont(FONT_TEKST);
        polje.setBackground(BOJA_KARTICE);
        polje.setForeground(BOJA_TEKSTA);
        polje.setCaretColor(BOJA_NAGLASKA);
        polje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return polje;
    }

    public static JPasswordField urediLozinkuPolje(JPasswordField polje) {
        polje.setFont(FONT_TEKST);
        polje.setBackground(BOJA_KARTICE);
        polje.setForeground(BOJA_TEKSTA);
        polje.setCaretColor(BOJA_NAGLASKA);
        polje.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return polje;
    }

    public static JLabel urediNaslovnuEtiketu(String tekst) {
        JLabel etiketa = new JLabel(tekst);
        etiketa.setFont(FONT_NASLOV);
        etiketa.setForeground(BOJA_TEKSTA);
        return etiketa;
    }

    public static JLabel urediEtiketu(String tekst) {
        JLabel etiketa = new JLabel(tekst);
        etiketa.setFont(FONT_TEKST);
        etiketa.setForeground(BOJA_TEKSTA_SLABIJA);
        return etiketa;
    }

    public static JPanel napraviKarticuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BOJA_PANELA);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOJA_OBRUBA, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        return panel;
    }

    public static void urediTabbedPane(JTabbedPane tPane) {
        tPane.setBackground(BOJA_PANELA);
        tPane.setForeground(BOJA_TEKSTA);
        tPane.setFont(FONT_GUMB);
    }

    public static void urediComboBox(JComboBox<?> combo) {
        combo.setFont(FONT_TEKST);
        combo.setBackground(BOJA_KARTICE);
        combo.setForeground(BOJA_TEKSTA);
        combo.setBorder(BorderFactory.createLineBorder(BOJA_OBRUBA, 1));
    }

    public static Border napraviNaslovniOkvir(String naslov) {
        TitledBorder okvir = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BOJA_OBRUBA, 1),
                naslov
        );
        okvir.setTitleFont(FONT_PODNASLOV);
        okvir.setTitleColor(BOJA_NAGLASKA);
        return okvir;
    }

    public static ImageIcon ucitajIkonu(String imeIkone) {
        try {
            java.io.File datoteka = new java.io.File("icons/" + imeIkone);
            if (datoteka.exists()) {
                return new ImageIcon(datoteka.getAbsolutePath());
            }
            datoteka = new java.io.File("../icons/" + imeIkone);
            if (datoteka.exists()) {
                return new ImageIcon(datoteka.getAbsolutePath());
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static ImageIcon ucitajIkonuSkaliranu(String imeIkone, int sirina, int visina) {
        ImageIcon ikona = ucitajIkonu(imeIkone);
        if (ikona != null) {
            Image slika = ikona.getImage().getScaledInstance(sirina, visina, Image.SCALE_SMOOTH);
            return new ImageIcon(slika);
        }
        return null;
    }
}