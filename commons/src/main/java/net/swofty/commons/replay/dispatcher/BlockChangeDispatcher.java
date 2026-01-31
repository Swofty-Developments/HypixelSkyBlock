package net.swofty.commons.replay.dispatcher;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.replay.ReplayRecorder;
import net.swofty.commons.replay.recordable.RecordableBlockChange;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlockChangeDispatcher implements ReplayDispatcher {
    private ReplayRecorder recorder;
    private Instance instance;

    // Queue of pending block changes
    private final Queue<PendingBlockChange> pendingChanges = new ConcurrentLinkedQueue<>();

    public BlockChangeDispatcher(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void initialize(ReplayRecorder recorder) {
        this.recorder = recorder;
    }

    public void recordBlockChange(int x, int y, int z, Block previousBlock, Block newBlock) {
        pendingChanges.offer(new PendingBlockChange(
                x, y, z,
                newBlock.stateId(),
                previousBlock.stateId()
        ));
    }

    public void recordBlockChange(Point point, Block previousBlock, Block newBlock) {
        recordBlockChange(point.blockX(), point.blockY(), point.blockZ(), previousBlock, newBlock);
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

    private record PendingBlockChange(int x, int y, int z, int newBlockStateId, int previousBlockStateId) {}
}
