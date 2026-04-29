import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages the list of valid system users.
 *
 * Reads user data from a comma-separated text file (users.txt),
 * validates each entry using the User constructor, and stores
 * only valid users. Also provides login lookup functionality.
 */
public class UserManager {

    /** Internal list storing all successfully validated users. */
    private ArrayList<User> users;

    /**
     * Constructs a new UserManager with an empty user list.
     */
    public UserManager() {
        users = new ArrayList<>();
    }

    /**
     * Loads users from a file into the internal list.
     *
     * Each line in the file must follow the format: username,password
     * Lines that are malformed or contain invalid credentials are silently skipped.
     * The list is cleared before each load to avoid duplicates.
     *
     * @param filePath path to the users text file (e.g. "users.txt")
     */
    public void loadUsers(String filePath) {
        users.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                try {
                    // Expect exactly two comma-separated fields per line
                    String[] parts = line.split(",");

                    if (parts.length != 2) {
                        continue; // Skip malformed lines
                    }

                    String username = parts[0].trim();
                    String password = parts[1].trim();

                    // User constructor throws if credentials are invalid
                    User user = new User(username, password);
                    users.add(user);

                } catch (IllegalArgumentException e) {
                    // Invalid credentials — skip this entry silently
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether a given username and password match any stored user.
     *
     * @param username the username to check
     * @param password the password to check
     * @return true if a matching user exists, false otherwise
     */
    public boolean isValidLogin(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the User object matching the given credentials.
     *
     * @param username the username to search for
     * @param password the password to match
     * @return the matching User object, or null if no match is found
     */
    public User getUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Returns the full list of loaded valid users.
     *
     * @return ArrayList of valid User objects
     */
    public ArrayList<User> getUsers() {
        return users;
    }
}