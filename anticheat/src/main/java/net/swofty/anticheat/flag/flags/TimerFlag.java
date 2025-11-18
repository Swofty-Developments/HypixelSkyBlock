package net.swofty.anticheat.flag.flags;

import net.swofty.anticheat.engine.PlayerTickInformation;
import net.swofty.anticheat.engine.SwoftyPlayer;
import net.swofty.anticheat.event.ListenerMethod;
import net.swofty.anticheat.event.events.PlayerPositionUpdateEvent;
import net.swofty.anticheat.flag.Flag;
import net.swofty.anticheat.loader.SwoftyAnticheat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimerFlag extends Flag {
    // Track last packet times per player
    private static final Map<UUID, Long> lastPacketTimes = new HashMap<>();
    private static final Map<UUID, Integer> packetCounts = new HashMap<>();

    // Normal tick length is 50ms
    private static final long EXPECTED_TICK_MS = 50;
    private static final double MIN_TICK_RATIO = 0.7; // 70% of normal speed (35ms) is suspicious
    private static final int SAMPLE_SIZE = 20; // Check over 20 ticks

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        SwoftyPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();
        long currentTime = System.currentTimeMillis();

        if (!lastPacketTimes.containsKey(uuid)) {
            lastPacketTimes.put(uuid, currentTime);
            packetCounts.put(uuid, 0);
            return;
        }

        long lastTime = lastPacketTimes.get(uuid);
        long timeDiff = currentTime - lastTime;

        // Update tracking
        lastPacketTimes.put(uuid, currentTime);
        int count = packetCounts.getOrDefault(uuid, 0) + 1;
        packetCounts.put(uuid, count);

        // Only check after we have enough samples
        if (count < SAMPLE_SIZE) {
            return;
        }

        // Calculate average tick time over recent samples
        java.util.List<PlayerTickInformation> ticks = player.getLastTicks();
        if (ticks.size() < 5) return;

        // Check if packets are coming too fast
        double tickRatio = (double) timeDiff / EXPECTED_TICK_MS;

        if (tickRatio < MIN_TICK_RATIO) {
            // Player is sending packets faster than normal game speed
            // This indicates timer/game speed manipulation
            double speedMultiplier = MIN_TICK_RATIO / tickRatio;

            // Higher speed = higher certainty
            // 1.5x speed = 70%, 2x speed = 85%, 3x+ = 95%
            double certainty = Math.min(0.95, 0.5 + (speedMultiplier - 1.0) * 0.3);

            player.flag(net.swofty.anticheat.flag.FlagType.TIMER, certainty);
        }

        // Reset counter periodically
        if (count >= SAMPLE_SIZE * 2) {
            packetCounts.put(uuid, 0);
        }
    }
}
