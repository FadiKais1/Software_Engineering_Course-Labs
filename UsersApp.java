import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class UsersApp {
    public static void main(String[] args) {
        // List to store only valid users
        ArrayList<User> validUsers = new ArrayList<>();

        // Try to read from the input file
        try (Scanner scanner = new Scanner(new File("users.txt"))) {

            // Read file line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split line into username and password
                String[] parts = line.trim().split("\\s+");

                // Skip lines that don't contain exactly two parts
                if (parts.length != 2) {
                    continue;
                }

                try {
                    // Create a User object (validation happens in constructor)
                    User user = new User(parts[0], parts[1]);

                    // Add valid user to the list
                    validUsers.add(user);
                } catch (IllegalArgumentException e) {
                    // Ignore invalid users (do not add them)
                }
            }
        } catch (FileNotFoundException e) {
            // Handle case where input file does not exist
            System.out.println("Input file not found.");
            return;
        }

        // Sort valid users by username
        validUsers.sort(Comparator.comparing(User::getName));

        // Print all valid users
        for (User user : validUsers) {
            System.out.println(user);
        }
    }
}