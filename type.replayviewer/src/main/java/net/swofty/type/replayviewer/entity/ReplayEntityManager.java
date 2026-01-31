package net.swofty.type.replayviewer.entity;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.InstanceContainer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReplayEntityManager {
    private final InstanceContainer instance;
    private final Map<Integer, Entity> entitiesById = new ConcurrentHashMap<>();
    private final Map<UUID, Entity> entitiesByUuid = new ConcurrentHashMap<>();

    public ReplayEntityManager(InstanceContainer instance) {
        this.instance = instance;
    }

    /**
     * Spawns an entity in the replay.
     */
    public void spawnEntity(int entityId, Entity entity, Pos position) {
        // Remove any existing entity with this ID
        despawnEntity(entityId);

        entitiesById.put(entityId, entity);
        if (entity.getUuid() != null) {
            entitiesByUuid.put(entity.getUuid(), entity);
        }

        entity.setInstance(instance, position);
    }

    /**
     * Despawns an entity from the replay.
     */
    public void despawnEntity(int entityId) {
        Entity entity = entitiesById.remove(entityId);
        if (entity != null) {
            entitiesByUuid.remove(entity.getUuid());
            entity.remove();
        }
    }

    /**
     * Updates an entity's position.
     */
    public void updateEntityPosition(int entityId, Pos position) {
        Entity entity = entitiesById.get(entityId);
        if (entity != null) {
            entity.teleport(position);
        }
    }

    /**
     * Gets an entity by its ID.
     */
    public Entity getEntity(int entityId) {
        return entitiesById.get(entityId);
    }

    /**
     * Gets an entity by its UUID.
     */
    public Entity getEntity(UUID uuid) {
        return entitiesByUuid.get(uuid);
    }

    /**
     * Checks if an entity exists.
     */
    public boolean hasEntity(int entityId) {
        return entitiesById.containsKey(entityId);
    }

    /**
     * Gets all entity IDs.
     */
    public Iterable<Integer> getEntityIds() {
        return entitiesById.keySet();
    }

    /**
     * Cleans up all entities.
     */
    public void cleanup() {
        for (Entity entity : entitiesById.values()) {
            entity.remove();
        }
        entitiesById.clear();
        entitiesByUuid.clear();
    }
}
