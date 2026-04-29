/**
 * Thread B — responsible for checking whether a user is currently blocked
 * before allowing a successful login to proceed.
 *
 * When the user enters correct credentials, this thread is created and
 * started. It checks the LoginAttemptManager to see if the email is
 * currently blocked. The result is stored and can be retrieved by the
 * LoginController after the thread finishes.
 *
 * Using a separate thread for this check follows the lab requirement
 * of separating the block-checking logic from the main JavaFX UI thread.
 */
public class BlockCheckerThread extends Thread {

    /** The email address to check for a block. */
    private String email;

    /** The shared attempt manager that holds block state for all users. */
    private LoginAttemptManager attemptManager;

    /**
     * Stores the result of the block check.
     * true = user is currently blocked, false = user is allowed in.
     */
    private boolean blocked;

    /**
     * Constructs a new BlockCheckerThread.
     *
     * @param email          the email address to check
     * @param attemptManager the shared LoginAttemptManager instance
     */
    public BlockCheckerThread(String email, LoginAttemptManager attemptManager) {
        this.email = email;
        this.attemptManager = attemptManager;
        this.blocked = false;
    }

    /**
     * Runs the thread — checks whether the given email is currently blocked.
     *
     * Delegates to LoginAttemptManager.isBlocked(), which is synchronized
     * to safely handle concurrent access from multiple threads.
     */
    @Override
    public void run() {
        System.out.println("[BlockCheckerThread] Checking block status for: " + email);
        blocked = attemptManager.isBlocked(email);
        System.out.println("[BlockCheckerThread] Is blocked: " + blocked);
    }

    /**
     * Returns the result of the block check.
     *
     * Should only be called after the thread has finished running
     * (i.e., after calling join() on this thread).
     *
     * @return true if the user is blocked, false if they are allowed in
     */
    public boolean isBlocked() {
        return blocked;
    }
}