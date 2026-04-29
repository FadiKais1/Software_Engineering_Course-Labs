/**
 * Represents a system user with a validated username (email) and password.
 *
 * The constructor enforces strict validation rules on both fields.
 * If either value is invalid, an IllegalArgumentException is thrown
 * with a descriptive message. This ensures that only properly formed
 * User objects can be created and stored in the system.
 */
public class User {

    /** The user's email address, used as their unique login identifier. */
    private String username;

    /** The user's password, must meet length and character requirements. */
    private String password;

    /**
     * Constructs a new User after validating the given credentials.
     *
     * Validation is performed before any values are assigned.
     * If username or password is invalid, an exception is thrown
     * and no User object is created.
     *
     * @param username the email address to use as the username
     * @param password the password for this user
     * @throws IllegalArgumentException if the username or password is invalid
     */
    public User(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username (email address) of this user.
     *
     * @return the username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this user.
     *
     * @return the password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Validates the given username as a properly formatted email address.
     *
     * Rules enforced:
     * - Must not be null or empty
     * - Maximum length of 50 characters
     * - Must contain exactly one '@' symbol, not at the start or end
     * - Local part (before '@'): letters, digits, and any of: . _ - + %
     * - Domain part (between '@' and last '.'): must start with letter or digit,
     *   allows letters, digits, '.', and '-'
     * - TLD (after last '.'): at least 2 letters only
     *
     * @param username the username to validate
     * @throws IllegalArgumentException if the username does not meet the rules
     */
    private void validateUsername(String username) {
        // Null or empty check
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Maximum length check
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username is too long, try something shorter");
        }

        // Locate the '@' symbol — must exist, not be first or last char, and appear only once
        int atIndex = username.indexOf('@');
        if (atIndex <= 0 || atIndex != username.lastIndexOf('@') || atIndex == username.length() - 1) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Split into local part and everything after '@'
        String localPart = username.substring(0, atIndex);
        String domainAndTld = username.substring(atIndex + 1);

        // Locate the last '.' to separate domain from TLD
        int lastDotIndex = domainAndTld.lastIndexOf('.');
        if (lastDotIndex <= 0 || lastDotIndex == domainAndTld.length() - 1) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        String domainPart = domainAndTld.substring(0, lastDotIndex);
        String tldPart = domainAndTld.substring(lastDotIndex + 1);

        // All three parts must be non-empty
        if (localPart.isEmpty() || domainPart.isEmpty() || tldPart.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // TLD must be at least 2 characters
        if (tldPart.length() < 2) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Validate local part characters: letters, digits, or . _ - + %
        for (int i = 0; i < localPart.length(); i++) {
            char c = localPart.charAt(i);
            if (!Character.isLetterOrDigit(c) && "._-+%".indexOf(c) == -1) {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }

        // Domain must start with a letter or digit
        if (!Character.isLetterOrDigit(domainPart.charAt(0))) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Validate domain part characters: letters, digits, '.', or '-'
        for (int i = 0; i < domainPart.length(); i++) {
            char c = domainPart.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '.' && c != '-') {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }

        // TLD must contain only letters
        for (int i = 0; i < tldPart.length(); i++) {
            char c = tldPart.charAt(i);
            if (!Character.isLetter(c)) {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }
    }

    /**
     * Validates the given password against the system's password policy.
     *
     * Rules enforced:
     * - Must not be null or empty
     * - Length between 8 and 12 characters (inclusive)
     * - No whitespace characters allowed
     * - Must contain at least one letter, one digit, and one symbol
     *   (any non-letter, non-digit, non-whitespace character counts as a symbol)
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if the password does not meet the rules
     */
    private void validatePassword(String password) {
        // Null or empty check
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid password");
        }

        // Minimum length check
        if (password.length() < 8) {
            throw new IllegalArgumentException("Your password is too short, add more characters");
        }

        // Maximum length check
        if (password.length() > 12) {
            throw new IllegalArgumentException("Your password is too long, try a shorter one");
        }

        // Track required character types
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                // Non-letter, non-digit, non-whitespace = valid symbol
                hasSymbol = true;
            } else {
                // Whitespace is strictly forbidden
                throw new IllegalArgumentException("Please enter a valid password");
            }
        }

        // All three character types must be present
        if (!hasLetter || !hasDigit || !hasSymbol) {
            throw new IllegalArgumentException("Please enter a valid password");
        }
    }

    /**
     * Returns a string representation of this user in the format: username password
     *
     * @return the username and password separated by a space
     */
    @Override
    public String toString() {
        return username + " " + password;
    }
}