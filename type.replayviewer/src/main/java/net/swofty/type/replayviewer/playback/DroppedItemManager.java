package net.swofty.type.replayviewer.playback;

import net.swofty.type.replayviewer.entity.ReplayEntityManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DroppedItemManager {

    private final ReplaySession session;
    private final ReplayEntityManager entityManager;
    private final Map<Integer, Integer> itemDespawnTicks = new HashMap<>();

    public DroppedItemManager(ReplaySession session) {
        this.session = session;
        this.entityManager = session.getEntityManager();
    }

    /**
     * Tracks a dropped item for lifecycle management.
     *
     * @param entityId    the item entity ID
     * @param despawnTick the tick when the item should despawn (0 = manual only)
     */
    public void trackItem(int entityId, int despawnTick) {
        if (despawnTick > 0) {
            itemDespawnTicks.put(entityId, despawnTick);
        }
    }

    /**
     * Stops tracking an item (e.g., when picked up).
     *
     * @param entityId the item entity ID
     */
    public void untrackItem(int entityId) {
        itemDespawnTicks.remove(entityId);
    }

    /**
     * Ticks the manager, despawning items that have expired.
     *
     * @param currentTick the current replay tick
     */
    public void tick(int currentTick) {
        Set<Integer> toDespawn = new HashSet<>();

        for (Map.Entry<Integer, Integer> entry : itemDespawnTicks.entrySet()) {
            if (currentTick >= entry.getValue()) {
                toDespawn.add(entry.getKey());
            }
        }

        for (Integer entityId : toDespawn) {
            entityManager.despawnEntity(entityId);
            itemDespawnTicks.remove(entityId);
        }
    }

    /**
     * Gets the despawn tick for an item.
     *
     * @param entityId the item entity ID
     * @return the despawn tick, or -1 if not tracked
     */
    public int getDespawnTick(int entityId) {
        return itemDespawnTicks.getOrDefault(entityId, -1);
    }

    /**
     * Checks if an item is being tracked.
     *
     * @param entityId the item entity ID
     * @return true if the item is tracked
     */
    public boolean isTracked(int entityId) {
        return itemDespawnTicks.containsKey(entityId);
    }

    /**
     * Gets all tracked item entity IDs.
     *
     * @return the set of tracked item entity IDs
     */
    public Set<Integer> getTrackedItems() {
        return new HashSet<>(itemDespawnTicks.keySet());
    }

    /**
     * Clears all tracked items.
     */
    public void clear() {
        itemDespawnTicks.clear();
    }

    /**
     * Seeks to a specific tick.
     * Items that should have despawned by this tick are removed.
     *
     * @param targetTick the tick to seek to
     */
    public void seekTo(int targetTick) {
        Set<Integer> toDespawn = new HashSet<>();

        for (Map.Entry<Integer, Integer> entry : itemDespawnTicks.entrySet()) {
            if (targetTick >= entry.getValue()) {
                toDespawn.add(entry.getKey());
            }
        }

        for (Integer entityId : toDespawn) {
            entityManager.despawnEntity(entityId);
            itemDespawnTicks.remove(entityId);
        }
    }
}
