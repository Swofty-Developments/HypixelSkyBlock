package net.swofty.type.bedwarsgame.replay;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.swofty.commons.replay.ReplayRecorder;
import net.swofty.commons.replay.dispatcher.ReplayDispatcher;
import net.swofty.commons.replay.recordable.RecordableEntityDespawn;
import net.swofty.commons.replay.recordable.RecordableEntitySpawn;
import net.swofty.commons.replay.recordable.RecordablePlayerArmSwing;
import net.swofty.commons.replay.recordable.RecordablePlayerSneak;
import net.swofty.commons.replay.recordable.RecordablePlayerSprint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EntityLifecycleDispatcher implements ReplayDispatcher {
    private ReplayRecorder recorder;
    private final Instance instance;

    // Track which entities we've seen
    private final Set<Integer> trackedEntities = new HashSet<>();

    // Track player states for change detection
    private final Map<Integer, PlayerState> playerStates = new HashMap<>();

    public EntityLifecycleDispatcher(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;

        // Record initial entities
        for (Entity entity : instance.getEntities()) {
            recordEntitySpawn(entity);
            trackedEntities.add(entity.getEntityId());

            if (entity instanceof Player player) {
                playerStates.put(entity.getEntityId(), new PlayerState(
                    player.isSneaking(), player.isSprinting()
                ));
            }
        }
    }

    @Override
    public void tick() {
        Set<Integer> currentEntities = new HashSet<>();

        // Check for new entities and state changes
        for (Entity entity : instance.getEntities()) {
            int entityId = entity.getEntityId();
            currentEntities.add(entityId);

            if (!trackedEntities.contains(entityId)) {
                // New entity spawned
                recordEntitySpawn(entity);
                trackedEntities.add(entityId);

                if (entity instanceof Player player) {
                    playerStates.put(entityId, new PlayerState(
                        player.isSneaking(), player.isSprinting()
                    ));
                }
            }

            // Check for player state changes
            if (entity instanceof Player player) {
                checkPlayerStateChanges(player);
            }
        }

        // Check for despawned entities
        Set<Integer> despawned = new HashSet<>(trackedEntities);
        despawned.removeAll(currentEntities);

        for (int entityId : despawned) {
            recorder.record(new RecordableEntityDespawn(entityId));
            trackedEntities.remove(entityId);
            playerStates.remove(entityId);
        }
    }

    private void recordEntitySpawn(Entity entity) {
        UUID entityUuid = entity.getUuid();
        int entityTypeId = entity.getEntityType().id();

        RecordableEntitySpawn spawn = new RecordableEntitySpawn(
            entity.getEntityId(),
            entityUuid,
            entityTypeId,
            entity.getPosition().x(),
            entity.getPosition().y(),
            entity.getPosition().z(),
            entity.getPosition().yaw(),
            entity.getPosition().pitch()
        );

        recorder.record(spawn);
    }

    private void checkPlayerStateChanges(Player player) {
        int entityId = player.getEntityId();
        PlayerState current = playerStates.get(entityId);

        if (current == null) {
            current = new PlayerState(false, false);
            playerStates.put(entityId, current);
        }

        // Check sneaking
        if (player.isSneaking() != current.sneaking) {
            recorder.record(new RecordablePlayerSneak(entityId, player.isSneaking()));
            playerStates.put(entityId, new PlayerState(player.isSneaking(), current.sprinting));
            current = playerStates.get(entityId);
        }

        // Check sprinting
        if (player.isSprinting() != current.sprinting) {
            recorder.record(new RecordablePlayerSprint(entityId, player.isSprinting()));
            playerStates.put(entityId, new PlayerState(current.sneaking, player.isSprinting()));
        }
    }

    public void recordArmSwing(int entityId, boolean mainHand) {
        recorder.record(new RecordablePlayerArmSwing(entityId, mainHand));
    }

    @Override
    public void cleanup() {
        trackedEntities.clear();
        playerStates.clear();
    }

    @Override
    public String getName() {
        return "EntityLifecycle";
    }

    private record PlayerState(boolean sneaking, boolean sprinting) {
    }
}
