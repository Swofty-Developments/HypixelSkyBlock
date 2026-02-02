package net.swofty.type.replayviewer.playback;

import net.minestom.server.instance.block.Block;
import net.swofty.commons.replay.recordable.Recordable;
import net.swofty.commons.replay.recordable.RecordableEntityDespawn;
import net.swofty.commons.replay.recordable.RecordableEntitySpawn;
import net.swofty.commons.replay.recordable.RecordableType;
import org.tinylog.Logger;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReplaySeeker {

    public static void seekForward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        // 1. Apply all block changes
        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(currentTick + 1, targetTick);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.newStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        // 2. Handle entity spawns and despawns
        Set<Integer> spawnedEntities = new HashSet<>();
        Set<Integer> despawnedEntities = new HashSet<>();

        List<Recordable> recordables = data.getRecordablesBetween(currentTick + 1, targetTick);
        for (Recordable rec : recordables) {
            if (rec instanceof RecordableEntitySpawn spawn) {
                spawnedEntities.add(spawn.getEntityId());
                despawnedEntities.remove(spawn.getEntityId());
            } else if (rec instanceof RecordableEntityDespawn despawn) {
                despawnedEntities.add(despawn.getEntityId());
                spawnedEntities.remove(despawn.getEntityId());
            }
        }

        // Despawn entities that were removed
        for (int entityId : despawnedEntities) {
            session.getEntityManager().despawnEntity(entityId);
        }

        // Spawn entities that appeared
        for (int entityId : spawnedEntities) {
            for (Recordable rec : recordables) {
                if (rec instanceof RecordableEntitySpawn spawn && spawn.getEntityId() == entityId) {
                    RecordablePlayer.play(spawn, session);
                    break;
                }
            }
        }

        // 3. Apply latest entity states
        applyLatestEntityStates(session, recordables);

        Logger.debug("Seeked forward from {} to {} ({} recordables)", currentTick, targetTick, recordables.size());
    }

    /**
     * Seeks backward from currentTick to targetTick.
     */
    public static void seekBackward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        // 1. Revert block changes in reverse order
        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(targetTick + 1, currentTick);
        Collections.reverse(blockChanges);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.previousStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        // 2. Handle entity spawns/despawns in reverse
        Set<Integer> toSpawn = new HashSet<>();
        Set<Integer> toDespawn = new HashSet<>();

        List<Recordable> recordables = data.getRecordablesBetween(targetTick + 1, currentTick);
        Collections.reverse(recordables);

        for (Recordable rec : recordables) {
            if (rec instanceof RecordableEntitySpawn spawn) {
                // Entity was spawned in this range - despawn it
                toDespawn.add(spawn.getEntityId());
                toSpawn.remove(spawn.getEntityId());
            } else if (rec instanceof RecordableEntityDespawn despawn) {
                // Entity was despawned in this range - need to respawn it
                toSpawn.add(despawn.getEntityId());
                toDespawn.remove(despawn.getEntityId());
            }
        }

        // Despawn entities that were spawned after target tick
        for (int entityId : toDespawn) {
            session.getEntityManager().despawnEntity(entityId);
        }

        // Respawn entities that were despawned after target tick
        for (int entityId : toSpawn) {
            respawnEntityAtTick(session, entityId, targetTick);
        }

        // 3. Apply entity states at target tick
        applyEntityStatesAtTick(session, targetTick);

        Logger.debug("Seeked backward from {} to {}", currentTick, targetTick);
    }

    private static void applyLatestEntityStates(ReplaySession session, List<Recordable> recordables) {
        // Group entity states by entity and type, keeping only the latest
        Map<Integer, Map<RecordableType, Recordable>> latestStates = new HashMap<>();

        for (Recordable rec : recordables) {
            if (rec.isEntityState() && rec.getEntityId() >= 0) {
                latestStates.computeIfAbsent(rec.getEntityId(), _ -> new EnumMap<>(RecordableType.class))
                        .put(rec.getType(), rec);
            }
        }

        // Apply latest states
        for (var entry : latestStates.entrySet()) {
            for (Recordable rec : entry.getValue().values()) {
                RecordablePlayer.play(rec, session);
            }
        }
    }

    private static void applyEntityStatesAtTick(ReplaySession session, int targetTick) {
        // Get all recordables up to target tick and find latest states for each entity
        ReplayData data = session.getReplayData();
        List<Recordable> allRecordables = data.getRecordablesBetween(0, targetTick);

        // Group by entity and state type, keeping latest
        Map<Integer, Map<RecordableType, Recordable>> states = new HashMap<>();

        for (Recordable rec : allRecordables) {
            if (rec.isEntityState() && rec.getEntityId() >= 0) {
                states.computeIfAbsent(rec.getEntityId(), _ -> new EnumMap<>(RecordableType.class))
                        .put(rec.getType(), rec);
            }
        }

        // Apply states
        for (var entry : states.entrySet()) {
            for (Recordable rec : entry.getValue().values()) {
                RecordablePlayer.play(rec, session);
            }
        }
    }

    private static void respawnEntityAtTick(ReplaySession session, int entityId, int targetTick) {
        // Find the last spawn event for this entity before targetTick
        ReplayData data = session.getReplayData();
        List<Recordable> recordables = data.getRecordablesBetween(0, targetTick);

        RecordableEntitySpawn lastSpawn = null;
        for (Recordable rec : recordables) {
            if (rec instanceof RecordableEntitySpawn spawn && spawn.getEntityId() == entityId) {
                lastSpawn = spawn;
            }
        }

        if (lastSpawn != null) {
            RecordablePlayer.play(lastSpawn, session);
        }
    }
}
