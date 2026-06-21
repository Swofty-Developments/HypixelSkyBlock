package net.swofty.type.generic.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.function.Consumer;

public class FloatingBlockEntity extends Entity {

    private final Pos initialPos;
    private Task blockDisplayRotation;

    public FloatingBlockEntity(Block block, float size, Instance instance, Pos pos, Consumer<BlockDisplayMeta> extraMeta) {
        super(EntityType.BLOCK_DISPLAY);
        this.initialPos = pos;

        editEntityMeta(BlockDisplayMeta.class, meta -> {
            meta.setBlockState(block);
            meta.setHasNoGravity(true);
            meta.setScale(new Vec(size, size, size));
            meta.setTranslation(new Vec(-size / 2, 0, -size / 2));
            meta.setPosRotInterpolationDuration(1);
            extraMeta.accept(meta);
        });

        setInstance(instance, pos);
    }

    public FloatingBlockEntity(Block block, float size, Instance instance, Pos pos) {
        this(block, size, instance, pos, _ -> {
        });
    }

    public void startAnimation() {
        if (blockDisplayRotation != null) {
            blockDisplayRotation.cancel();
        }

        blockDisplayRotation = MinecraftServer.getSchedulerManager().buildTask(() -> {
                final float BOB_AMPLITUDE = 0.25f;
                final float BOB_PERIOD_SECONDS = 4.0f;
                final float ROTATE_DEG_PER_SEC = 180f;

                double timeSeconds = System.currentTimeMillis() / 1000.0;
                double phase = (timeSeconds / BOB_PERIOD_SECONDS) * Math.PI * 2.0;
                float bobOffset = (float) (Math.sin(phase) * BOB_AMPLITUDE);
                boolean goingDown = Math.cos(phase) < 0;
                float rotation = (float) ((timeSeconds * ROTATE_DEG_PER_SEC) % 360.0);
                rotation = goingDown ? rotation : -rotation;

                teleport(new Pos(
                    initialPos.x(),
                    initialPos.y() + bobOffset,
                    initialPos.z(),
                    rotation,
                    0f
                ));
            }).delay(TaskSchedule.seconds(1))
            .repeat(TaskSchedule.tick(1))
            .schedule();
    }

    public void stopAnimation() {
        if (blockDisplayRotation != null) {
            blockDisplayRotation.cancel();
            blockDisplayRotation = null;
        }
    }

    @Override
    public void remove(boolean permanent) {
        stopAnimation();
        super.remove(permanent);
    }
}
