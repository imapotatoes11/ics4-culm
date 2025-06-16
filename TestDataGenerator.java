import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestDataGenerator {

    /**
     * Generates a SHA-256 hash for a given string.
     */
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

    /**
     * Helper method to convert a byte array into a hexadecimal string
     */
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

    public static void main(String[] args) {
        // Create test data file with dummy users
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("arcade.txt"))) {

            // Admin user (password: admin123)
            writer.write("admin\n");
            writer.write(generateSHA256("admin123") + "\n");
            writer.write("30\n");
            writer.write("Administrator\n");
            writer.write(":\n");

            // Test user 1 (password: password123)
            writer.write("john_doe\n");
            writer.write(generateSHA256("password123") + "\n");
            writer.write("25\n");
            writer.write("John Doe\n");
            writer.write(":\n");

            // Test user 2 (password: test456)
            writer.write("alice_smith\n");
            writer.write(generateSHA256("test456") + "\n");
            writer.write("22\n");
            writer.write("Alice Smith\n");
            writer.write(":\n");

            // Test user 3 (password: demo789)
            writer.write("bob_jones\n");
            writer.write(generateSHA256("demo789") + "\n");
            writer.write("28\n");
            writer.write("Bob Jones\n");
            writer.write(":\n");

            // Test user 4 (password: user123)
            writer.write("charlie_brown\n");
            writer.write(generateSHA256("user123") + "\n");
            writer.write("19\n");
            writer.write("Charlie Brown\n");
            writer.write(":\n");

            // Test user 5 (password: test123)
            writer.write("diana_prince\n");
            writer.write(generateSHA256("test123") + "\n");
            writer.write("24\n");
            writer.write("Diana Prince\n");
            writer.write(":\n");

            // Test user 6 (password: hello123)
            writer.write("eve_wilson\n");
            writer.write(generateSHA256("hello123") + "\n");
            writer.write("31\n");
            writer.write("Eve Wilson\n");
            writer.write(":\n");

            // Test user 7 (password: arcade123)
            writer.write("frank_miller\n");
            writer.write(generateSHA256("arcade123") + "\n");
            writer.write("27\n");
            writer.write("Frank Miller\n");
            writer.write(":\n");

            // Test user 8 (password: player123)
            writer.write("grace_lee\n");
            writer.write(generateSHA256("player123") + "\n");
            writer.write("23\n");
            writer.write("Grace Lee\n");
            writer.write(":\n");

            // End marker
            writer.write("end\n");

            System.out.println("Test data file 'arcade.txt' created successfully!");
            System.out.println("\nTest Users Created:");
            System.out.println("==================");
            System.out.println("Username: admin | Password: admin123 | Age: 30 | Name: Administrator");
            System.out.println("Username: john_doe | Password: password123 | Age: 25 | Name: John Doe");
            System.out.println("Username: alice_smith | Password: test456 | Age: 22 | Name: Alice Smith");
            System.out.println("Username: bob_jones | Password: demo789 | Age: 28 | Name: Bob Jones");
            System.out.println("Username: charlie_brown | Password: user123 | Age: 19 | Name: Charlie Brown");
            System.out.println("Username: diana_prince | Password: test123 | Age: 24 | Name: Diana Prince");
            System.out.println("Username: eve_wilson | Password: hello123 | Age: 31 | Name: Eve Wilson");
            System.out.println("Username: frank_miller | Password: arcade123 | Age: 27 | Name: Frank Miller");
            System.out.println("Username: grace_lee | Password: player123 | Age: 23 | Name: Grace Lee");

        } catch (IOException e) {
            System.err.println("Error creating test data file: " + e.getMessage());
        }
    }
}