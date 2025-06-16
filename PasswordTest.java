import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordTest {
    public static void main(String[] args) {
        String targetHash = "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9";
        String[] passwords = { "admin", "password", "123456", "secret", "admin123", "password123", "root", "test" };

        System.out.println("Testing passwords for admin account...");
        System.out.println("Target hash: " + targetHash);
        System.out.println();

        for (String pwd : passwords) {
            String hash = generateSHA256(pwd);
            System.out.println("Password: '" + pwd + "' -> Hash: " + hash);
            if (hash != null && hash.equals(targetHash)) {
                System.out.println("*** MATCH FOUND! Admin password is: " + pwd + " ***");
            }
        }
    }

    public static String generateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not found!");
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}