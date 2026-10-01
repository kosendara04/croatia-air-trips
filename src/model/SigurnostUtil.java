package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SigurnostUtil {

    private SigurnostUtil() {
    }

    public static String hesirajLozinku(String lozinka) {
        if (lozinka == null || lozinka.isEmpty()) {
            return "";
        }
        try {
            MessageDigest sazetakPoruke = MessageDigest.getInstance("SHA-256");
            byte[] hashBajtovi = sazetakPoruke.digest(lozinka.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBajtovi) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Greska pri hesiranju lozinke", e);
        }
    }
}
