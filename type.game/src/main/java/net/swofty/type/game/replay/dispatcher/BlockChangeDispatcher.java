package net.swofty.type.game.replay.dispatcher;

import net.swofty.type.game.replay.ReplayRecorder;
import net.swofty.type.game.replay.recordable.RecordableBlockChange;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockChangeDispatcher implements ReplayDispatcher {
    private ReplayRecorder recorder;

    private final Queue<PendingBlockChange> pendingChanges = new ConcurrentLinkedQueue<>();

    public BlockChangeDispatcher() {
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;
    }

    public void recordBlockChange(int x, int y, int z, int previousBlock, int newBlock) {
        pendingChanges.offer(new PendingBlockChange(
            x, y, z,
            newBlock,
            previousBlock
        ));
    }

    @Override
    public void tick() {
        PendingBlockChange change;
        while ((change = pendingChanges.poll()) != null) {
            recorder.record(new RecordableBlockChange(
                change.x, change.y, change.z,
                change.newBlockStateId,
                change.previousBlockStateId
            ));
        }
    }

    @Override
    public void cleanup() {
        pendingChanges.clear();
    }

    @Override
    public String getName() {
        return "BlockChange";
    }

    private record PendingBlockChange(int x, int y, int z, int newBlockStateId, int previousBlockStateId) {
    }
}
