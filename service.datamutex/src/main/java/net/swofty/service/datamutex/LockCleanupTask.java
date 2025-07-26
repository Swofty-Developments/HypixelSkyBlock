package net.swofty.service.datamutex;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LockCleanupTask {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startCleanupTask() {
        // Clean up expired locks every 10 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                DataLockManager.cleanupExpiredLocks();
            } catch (Exception e) {
                System.err.println("Error during lock cleanup: " + e.getMessage());
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}