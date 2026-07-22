package net.swofty.commons;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

public final class ServerMemory {

    private static final double MIB = 1024.0 * 1024.0;

    public static double getUsed() {
        MemoryUsage heap = ManagementFactory
                .getMemoryMXBean()
                .getHeapMemoryUsage();

        double used = heap.getUsed();
        return used / MIB;
    }
}