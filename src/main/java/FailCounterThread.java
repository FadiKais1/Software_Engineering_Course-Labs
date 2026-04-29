/**
 * Thread A — responsible for recording failed login attempts.
 *
 * Every time the user enters wrong credentials, this thread is
 * created and started. It calls LoginAttemptManager to increment
 * the fail counter for the given email. If the counter reaches
 * the maximum (n), the manager automatically blocks that email.
 *
 * Using a separate thread for this task follows the lab requirement
 * of handling attempt tracking concurrently and independently from
 * the main JavaFX UI thread.
 */
public class FailCounterThread extends Thread {

    /** The email address that just failed a login attempt. */
    private String email;

    /** The shared attempt manager that tracks all users' fail counts. */
    private LoginAttemptManager attemptManager;

    /**
     * Constructs a new FailCounterThread.
     *
     * @param email          the email that failed the login attempt
     * @param attemptManager the shared LoginAttemptManager instance
     */
    public FailCounterThread(String email, LoginAttemptManager attemptManager) {
        this.email = email;
        this.attemptManager = attemptManager;
    }

    /**
     * Runs the thread — records the failed attempt for this email.
     *
     * Delegates to LoginAttemptManager.recordFailedAttempt(),
     * which is synchronized to safely handle concurrent access.
     */
    @Override
    public void run() {
        System.out.println("[FailCounterThread] Recording failed attempt for: " + email);
        attemptManager.recordFailedAttempt(email);
    }
}