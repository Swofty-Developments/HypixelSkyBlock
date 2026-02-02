package net.swofty.commons.replay.entity;

import net.swofty.commons.replay.recordable.Recordable;
import net.swofty.commons.replay.recordable.RecordableType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// this is slightly dumb but whatever - ARI
public class EntityStateTracker {
    // Maps: entityId -> stateType -> list of (tick, recordable) pairs
    private final Map<Integer, Map<StateType, TreeMap<Integer, Recordable>>> states = new HashMap<>();
    private final Map<StateType, Recordable> defaultStates = new EnumMap<>(StateType.class);

    // I don't know if I like this - ARI
    public enum StateType {
        LOCATION(RecordableType.ENTITY_LOCATIONS),
        EQUIPMENT_HELMET(RecordableType.ENTITY_EQUIPMENT),
        EQUIPMENT_CHESTPLATE(RecordableType.ENTITY_EQUIPMENT),
        EQUIPMENT_LEGGINGS(RecordableType.ENTITY_EQUIPMENT),
        EQUIPMENT_BOOTS(RecordableType.ENTITY_EQUIPMENT),
        EQUIPMENT_MAIN_HAND(RecordableType.ENTITY_EQUIPMENT),
        EQUIPMENT_OFF_HAND(RecordableType.ENTITY_EQUIPMENT),
        SNEAKING(RecordableType.PLAYER_SNEAK),
        SPRINTING(RecordableType.PLAYER_SPRINT),
        GAMEMODE(RecordableType.PLAYER_GAMEMODE),
        MOUNT(RecordableType.ENTITY_MOUNT),
        METADATA(RecordableType.ENTITY_METADATA),
        ;

        public final RecordableType recordableType;

        StateType(RecordableType recordableType) {
            this.recordableType = recordableType;
        }
    }

    public void recordState(int entityId, StateType stateType, int tick, Recordable recordable) {
        states.computeIfAbsent(entityId, k -> new EnumMap<>(StateType.class))
            .computeIfAbsent(stateType, k -> new TreeMap<>())
            .put(tick, recordable);
    }

    /**
     * Gets the state at a specific tick.
     * Returns the most recent state at or before the given tick.
     *
     * @param entityId  The entity ID
     * @param stateType The state type
     * @param tick      The tick to query
     * @return The state, or null if no state exists
     */
    public Recordable getStateAt(int entityId, StateType stateType, int tick) {
        Map<StateType, TreeMap<Integer, Recordable>> entityStates = states.get(entityId);
        if (entityStates == null) {
            return defaultStates.get(stateType);
        }

        TreeMap<Integer, Recordable> stateHistory = entityStates.get(stateType);
        if (stateHistory == null || stateHistory.isEmpty()) {
            return defaultStates.get(stateType);
        }

        // Get the floor entry (most recent at or before tick)
        Map.Entry<Integer, Recordable> entry = stateHistory.floorEntry(tick);
        if (entry == null) {
            return defaultStates.get(stateType);
        }

        return entry.getValue();
    }

    /**
     * Gets all state changes for an entity between two ticks.
     */
    public List<Recordable> getStatesBetween(int entityId, StateType stateType, int startTick, int endTick) {
        Map<StateType, TreeMap<Integer, Recordable>> entityStates = states.get(entityId);
        if (entityStates == null) return Collections.emptyList();

        TreeMap<Integer, Recordable> stateHistory = entityStates.get(stateType);
        if (stateHistory == null) return Collections.emptyList();

        return new ArrayList<>(stateHistory.subMap(startTick, true, endTick, true).values());
    }

    /**
     * Gets the latest state for each type for an entity at a given tick.
     * Used when seeking to a specific time.
     */
    public Map<StateType, Recordable> getAllStatesAt(int entityId, int tick) {
        Map<StateType, Recordable> result = new EnumMap<>(StateType.class);

        Map<StateType, TreeMap<Integer, Recordable>> entityStates = states.get(entityId);
        if (entityStates == null) {
            // Return defaults
            for (StateType type : StateType.values()) {
                Recordable def = defaultStates.get(type);
                if (def != null) result.put(type, def);
            }
            return result;
        }

        for (StateType type : StateType.values()) {
            Recordable state = getStateAt(entityId, type, tick);
            if (state != null) {
                result.put(type, state);
            }
        }

        return result;
    }

    public void setDefaultState(StateType stateType, Recordable defaultState) {
        defaultStates.put(stateType, defaultState);
    }

    public void removeEntity(int entityId) {
        states.remove(entityId);
        skinData.remove(entityId);
        healthData.remove(entityId);
    }

    public void clear() {
        states.clear();
        skinData.clear();
        healthData.clear();
    }

    public Set<Integer> getTrackedEntities() {
        return new HashSet<>(states.keySet());
    }

    private final Map<Integer, SkinData> skinData = new HashMap<>();

    public void trackSkin(int entityId, String textureValue, String textureSignature) {
        skinData.put(entityId, new SkinData(textureValue, textureSignature));
    }

    public SkinData getSkin(int entityId) {
        return skinData.get(entityId);
    }

    public record SkinData(String textureValue, String textureSignature) {
    }

    private final Map<Integer, HealthData> healthData = new HashMap<>();

    public void trackHealth(int entityId, float health, float maxHealth) {
        healthData.put(entityId, new HealthData(health, maxHealth));
    }

    public HealthData getHealth(int entityId) {
        return healthData.get(entityId);
    }

    public record HealthData(float health, float maxHealth) {
    }

    public void track(Recordable recordable) {
        if (!recordable.isEntityState() || recordable.getEntityId() < 0) return;

        StateType stateType = mapRecordableToStateType(recordable);
        if (stateType != null) {
            recordState(recordable.getEntityId(), stateType, 0, recordable);
        }
    }

    private StateType mapRecordableToStateType(Recordable recordable) {
        return switch (recordable.getType()) {
            case ENTITY_LOCATIONS -> StateType.LOCATION;
            case PLAYER_SNEAK -> StateType.SNEAKING;
            case PLAYER_SPRINT -> StateType.SPRINTING;
            case PLAYER_GAMEMODE -> StateType.GAMEMODE;
            case ENTITY_MOUNT -> StateType.MOUNT;
            case ENTITY_METADATA -> StateType.METADATA;
            default -> null;
        };
    }
}
