import java.util.HashMap;
import java.util.Map;

/**
 * Manages login attempt tracking for all users.
 *
 * For each email address, this class tracks:
 * - How many consecutive failed login attempts have occurred
 * - Whether the user is currently blocked
 * - The timestamp of when the block was applied
 *
 * This class is shared between Thread A (FailCounterThread) and
 * Thread B (BlockCheckerThread), so all methods are synchronized
 * to prevent race conditions between threads.
 */
public class LoginAttemptManager {

    /**
     * Stores the number of consecutive failed attempts per email.
     * Key = email address, Value = fail count
     */
    private Map<String, Integer> failCounts;

    /**
     * Stores the timestamp (ms) when a user was blocked.
     * Key = email address, Value = System.currentTimeMillis() at block time
     * If the user is not blocked, their email will not be in this map.
     */
    private Map<String, Long> blockTimestamps;

    /** Maximum number of failed attempts before a user is blocked. */
    private int maxAttempts;

    /** Duration in milliseconds that a user stays blocked. */
    private long blockDurationMs;

    /**
     * Constructs a new LoginAttemptManager.
     *
     * @param maxAttempts     maximum number of failed attempts before blocking (n)
     * @param blockDurationMs how long the user stays blocked in milliseconds (t * 1000)
     */
    public LoginAttemptManager(int maxAttempts, long blockDurationMs) {
        this.maxAttempts = maxAttempts;
        this.blockDurationMs = blockDurationMs;
        this.failCounts = new HashMap<>();
        this.blockTimestamps = new HashMap<>();
    }

    /**
     * Records a failed login attempt for the given email.
     *
     * Increments the fail counter for this email. If the counter
     * reaches maxAttempts, the user is immediately blocked and
     * the block timestamp is recorded.
     *
     * Called by Thread A (FailCounterThread).
     *
     * @param email the email address that failed to log in
     */
    public synchronized void recordFailedAttempt(String email) {
        // Get current fail count, defaulting to 0 if first attempt
        int count = failCounts.getOrDefault(email, 0) + 1;
        failCounts.put(email, count);

        // Block the user if they've reached the maximum attempts
        if (count >= maxAttempts) {
            blockTimestamps.put(email, System.currentTimeMillis());
            System.out.println("[LoginAttemptManager] User blocked: " + email);
        }
    }

    /**
     * Checks whether the given email is currently blocked.
     *
     * If the user is blocked but their block duration has expired,
     * they are automatically unblocked and their fail count is reset.
     *
     * Called by Thread B (BlockCheckerThread).
     *
     * @param email the email address to check
     * @return true if the user is currently blocked, false otherwise
     */
    public synchronized boolean isBlocked(String email) {
        if (!blockTimestamps.containsKey(email)) {
            return false; // Never blocked
        }

        long blockedAt = blockTimestamps.get(email);
        long elapsed = System.currentTimeMillis() - blockedAt;

        if (elapsed >= blockDurationMs) {
            // Block has expired — reset everything for this user
            blockTimestamps.remove(email);
            failCounts.put(email, 0);
            System.out.println("[LoginAttemptManager] Block expired, user unblocked: " + email);
            return false;
        }

        return true; // Still blocked
    }

    /**
     * Returns how many seconds remain in the current block for the given email.
     * Returns 0 if the user is not blocked.
     *
     * @param email the email address to check
     * @return remaining block time in seconds, or 0 if not blocked
     */
    public synchronized long getRemainingBlockSeconds(String email) {
        if (!blockTimestamps.containsKey(email)) {
            return 0;
        }

        long blockedAt = blockTimestamps.get(email);
        long elapsed = System.currentTimeMillis() - blockedAt;
        long remaining = blockDurationMs - elapsed;

        return remaining > 0 ? remaining / 1000 : 0;
    }

    /**
     * Returns the current fail count for the given email.
     *
     * @param email the email address to check
     * @return number of consecutive failed attempts
     */
    public synchronized int getFailCount(String email) {
        return failCounts.getOrDefault(email, 0);
    }

    /**
     * Returns the maximum number of attempts allowed before blocking.
     *
     * @return the maxAttempts value (n)
     */
    public int getMaxAttempts() {
        return maxAttempts;
    }
}