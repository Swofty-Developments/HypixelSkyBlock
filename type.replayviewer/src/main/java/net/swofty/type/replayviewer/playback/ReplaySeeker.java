package net.swofty.type.replayviewer.playback;

import net.minestom.server.instance.block.Block;
import net.swofty.type.game.replay.recordable.Recordable;
import org.tinylog.Logger;

import java.util.Collections;
import java.util.List;

public class ReplaySeeker {

    public static void seekForward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(currentTick + 1, targetTick);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.newStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        List<Recordable> recordables = data.getRecordablesBetween(currentTick + 1, targetTick);
        applyPersistentRecordables(session, recordables);

        Logger.debug("Seeked forward from {} to {} ({} recordables)", currentTick, targetTick, recordables.size());
    }

    public static void seekBackward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(targetTick + 1, currentTick);
        Collections.reverse(blockChanges);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.previousStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        rebuildStateAtTick(session, targetTick);

        Logger.debug("Seeked backward from {} to {}", currentTick, targetTick);
    }

    public static void rebuildStateAtTick(ReplaySession session, int targetTick) {
        ReplayData data = session.getReplayData();

        session.beginStateRebuild();
        try {
            session.getEntityManager().cleanup();
            session.getDroppedItemManager().clear();
            session.getDynamicTextManager().cleanup();
            session.getNpcManager().cleanup();
            session.getStateTracker().clear();

            List<Recordable> recordables = data.getRecordablesBetween(0, targetTick);
            applyPersistentRecordables(session, recordables);

            Logger.debug("Rebuilt replay state at tick {} ({} recordables)", targetTick, recordables.size());
        } finally {
            session.endStateRebuild();
        }
    }

    private static void applyPersistentRecordables(ReplaySession session, List<Recordable> recordables) {
        for (Recordable recordable : recordables) {
            if (!isPersistentRecordable(recordable)) {
                continue;
            }

            try {
                RecordablePlayer.play(recordable, session);
            } catch (Exception e) {
                Logger.error(e, "Failed to apply seek recordable {} at tick {}", recordable.getType(), recordable.getTick());
            }
        }
    }

    private static boolean isPersistentRecordable(Recordable recordable) {
        return switch (recordable.getType()) {
            case ENTITY_SPAWN,
                 ENTITY_DESPAWN,
                 ITEM_PICKUP,
                 PLAYER_RESPAWN,
                 ENTITY_EFFECT -> true;
            default -> recordable.isEntityState();
        };
    }
}
