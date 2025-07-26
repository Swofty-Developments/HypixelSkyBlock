package net.swofty.types.generic.redis.service.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerLockManager {
    private static final Map<String, Long> activeLocks = new ConcurrentHashMap<>();
    private static final long LOCK_TIMEOUT = 30000;

    public static boolean acquireLock(String lockKey) {
        long currentTime = System.currentTimeMillis();

        Long existingLock = activeLocks.get(lockKey);
        if (existingLock != null) {
            long lockAge = currentTime - existingLock;

            if (lockAge < LOCK_TIMEOUT) {
                return false;
            } else {
                activeLocks.remove(lockKey);
            }
        }

        activeLocks.put(lockKey, currentTime);
        return true;
    }

    public static boolean releaseLock(String lockKey) {
        Long removedLock = activeLocks.remove(lockKey);
        return removedLock != null;
    }

    public static void cleanupExpiredLocks() {
        long currentTime = System.currentTimeMillis();
        activeLocks.entrySet().removeIf(entry -> {
            long lockAge = currentTime - entry.getValue();
            return lockAge > LOCK_TIMEOUT;
        });
    }
}