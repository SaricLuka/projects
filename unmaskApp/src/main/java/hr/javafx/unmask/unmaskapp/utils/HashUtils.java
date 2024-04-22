package hr.javafx.unmask.unmaskapp.utils;
import at.favre.lib.crypto.bcrypt.BCrypt;
public class HashUtils {
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(14, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }
}
