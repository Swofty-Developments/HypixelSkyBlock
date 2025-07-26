package net.swofty.service.datamutex;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataLockManager {
    private static final Map<String, LockInfo> activeLocks = new ConcurrentHashMap<>();
    private static final long LOCK_TIMEOUT = 30000; // 30 seconds

    public static class LockInfo {
        public final UUID lockId;
        public final long timestamp;
        public final String requesterId;

        public LockInfo(UUID lockId, long timestamp, String requesterId) {
            this.lockId = lockId;
            this.timestamp = timestamp;
            this.requesterId = requesterId;
        }

        public boolean isExpired() {
            return (System.currentTimeMillis() - timestamp) > LOCK_TIMEOUT;
        }
    }

    /**
     * Attempts to acquire a lock for the given key
     */
    public static boolean acquireLock(String lockKey, String requesterId) {
        LockInfo existingLock = activeLocks.get(lockKey);

        // Clean up expired locks
        if (existingLock != null && existingLock.isExpired()) {
            activeLocks.remove(lockKey);
            existingLock = null;
        }

        // Check if already locked by someone else
        if (existingLock != null && !existingLock.requesterId.equals(requesterId)) {
            return false;
        }

        // Acquire or renew lock
        UUID lockId = UUID.randomUUID();
        activeLocks.put(lockKey, new LockInfo(lockId, System.currentTimeMillis(), requesterId));
        return true;
    }

    /**
     * Releases a lock for the given key
     */
    public static void releaseLock(String lockKey, String requesterId) {
        LockInfo lock = activeLocks.get(lockKey);
        if (lock != null && lock.requesterId.equals(requesterId)) {
            activeLocks.remove(lockKey);
        }
    }

    /**
     * Checks if a key is currently locked
     */
    public static boolean isLocked(String lockKey) {
        LockInfo lock = activeLocks.get(lockKey);
        if (lock == null) return false;

        if (lock.isExpired()) {
            activeLocks.remove(lockKey);
            return false;
        }

        return true;
    }

    /**
     * Gets the lock info for a key (or null if not locked)
     */
    public static LockInfo getLockInfo(String lockKey) {
        LockInfo lock = activeLocks.get(lockKey);
        if (lock != null && lock.isExpired()) {
            activeLocks.remove(lockKey);
            return null;
        }
        return lock;
    }

    /**
     * Cleans up all expired locks
     */
    public static void cleanupExpiredLocks() {
        activeLocks.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
}