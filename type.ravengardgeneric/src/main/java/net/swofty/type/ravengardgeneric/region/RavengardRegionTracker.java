package net.swofty.type.ravengardgeneric.region;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.HypixelGenericLoader;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.ravengardgeneric.event.custom.PlayerRegionChangeEvent;
import net.swofty.type.ravengardgeneric.user.RavengardPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Polls every player's region and fires {@link PlayerRegionChangeEvent} when it changes, mirroring
 * how SkyBlock drives region changes off a tracked player value.
 */
public final class RavengardRegionTracker {
    private static final Map<UUID, RavengardRegionType> LAST = new ConcurrentHashMap<>();

    private RavengardRegionTracker() {
    }

    public static void start() {
        MinecraftServer.getSchedulerManager()
                .buildTask(RavengardRegionTracker::tick)
                .repeat(TaskSchedule.tick(10))
                .schedule();
    }

    public static void forget(UUID uuid) {
        LAST.remove(uuid);
    }

    private static void tick() {
        for (HypixelPlayer online : HypixelGenericLoader.getLoadedPlayers()) {
            if (!(online instanceof RavengardPlayer player)) {
                continue;
            }

            RavengardRegion region = player.getRegion();
            RavengardRegionType to = region == null ? null : region.getType();
            RavengardRegionType from = LAST.get(player.getUuid());

            if (to == from) {
                continue;
            }

            // the map rejects nulls, and null is a real state -- outside every region
            if (to == null) {
                LAST.remove(player.getUuid());
            } else {
                LAST.put(player.getUuid(), to);
            }
            HypixelEventHandler.callCustomEvent(new PlayerRegionChangeEvent(player, from, to));
        }
    }
}
