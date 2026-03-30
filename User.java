public class User {
    // Private fields for encapsulation
    private String username;
    private String password;

    // Constructor - validates input before assigning values
    public User(String username, String password) {
        validateUsername(username);
        validatePassword(password);
        this.username = username;
        this.password = password;
    }

    // Getter for username (used for sorting)
    public String getName() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Validates that the username is a properly formatted email
    private void validateUsername(String username) {
        // Check for null or empty username
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Check maximum length
        if (username.length() > 50) {
            throw new IllegalArgumentException("Username is too long, try something shorter");
        }

        // Find '@' position
        int atIndex = username.indexOf('@');

        // Ensure exactly one '@' and not at start/end
        if (atIndex <= 0 || atIndex != username.lastIndexOf('@') || atIndex == username.length() - 1) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Split email into parts
        String localPart = username.substring(0, atIndex);
        String domainAndTld = username.substring(atIndex + 1);

        // Find last '.' for domain and TLD separation
        int lastDotIndex = domainAndTld.lastIndexOf('.');

        // Ensure valid '.' position
        if (lastDotIndex <= 0 || lastDotIndex == domainAndTld.length() - 1) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Extract domain and TLD
        String domainPart = domainAndTld.substring(0, lastDotIndex);
        String tldPart = domainAndTld.substring(lastDotIndex + 1);

        // Ensure no empty parts
        if (localPart.isEmpty() || domainPart.isEmpty() || tldPart.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // TLD must be at least 2 letters
        if (tldPart.length() < 2) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Validate characters in local part
        for (int i = 0; i < localPart.length(); i++) {
            char c = localPart.charAt(i);
            if (!Character.isLetterOrDigit(c) && "._-+%".indexOf(c) == -1) {
                throw new IllegalArgumentException("Please enter a valid Email as username");
            }
        }

        // Domain must start with letter or digit
        if (!Character.isLetterOrDigit(domainPart.charAt(0))) {
            throw new IllegalArgumentException("Please enter a valid Email as username");
        }

        // Validate characters in domain
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

    // Validates password based on length and character requirements
    private void validatePassword(String password) {
        // Check for null or empty password
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Please enter a valid password");
        }

        // Check minimum length
        if (password.length() < 8) {
            throw new IllegalArgumentException("Your password is too short, add more characters");
        }

        // Check maximum length
        if (password.length() > 12) {
            throw new IllegalArgumentException("Your password is too long, try a shorter one");
        }

        // Flags to ensure required character types exist
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        // Iterate over password characters
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isWhitespace(c)) {
                // Any non-whitespace character is treated as symbol
                hasSymbol = true;
            } else {
                // Whitespace is not allowed
                throw new IllegalArgumentException("Please enter a valid password");
            }
        }

        // Ensure all required character types are present
        if (!hasLetter || !hasDigit || !hasSymbol) {
            throw new IllegalArgumentException("Please enter a valid password");
        }
    }

    // Defines how the object is printed
    @Override
    public String toString() {
        return username + " " + password;
    }
}