package net.swofty.type.skyblockgeneric.region.mining;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.network.packet.client.play.ClientPlayerActionPacket;
import net.minestom.server.network.packet.server.play.*;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.event.HypixelEventHandler;
import net.swofty.type.skyblockgeneric.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class BreakingTask {
    private int counter = 0;
    private final SkyBlockPlayer player;
    private final PositionedBlock block;
    private final double breakTime;
    private TaskSchedule nextSchedule;
    private final int sequence;

    public BreakingTask(SkyBlockPlayer player, PositionedBlock block, SkyBlockItem item, int sequence) {
        this.player = player;
        this.block = block;
        this.sequence = sequence;

        double miningTime = player.getTimeToMine(item, block.block());

        if (miningTime == -1) {
            // Player can't mine this block - keep running to stall progress
            sendBlockBreak(0);
            this.breakTime = -1;
            this.nextSchedule = TaskSchedule.tick(1);
            return;
        }

        this.breakTime = miningTime;
        this.nextSchedule = TaskSchedule.tick(1);
    }

    public TaskSchedule run() {
        if (nextSchedule.equals(TaskSchedule.stop())) {
            sendBlockBreak(-1);
            return TaskSchedule.stop();
        }

        // If player can't mine this block, continuously send progress 0 to stall
        if (breakTime == -1) {
            sendBlockBreak(0);
            return TaskSchedule.tick(1);
        }

        int dmg = (int) ((counter / (float) breakTime) * 10);
        sendBlockBreak(dmg);
        counter += 1;

        if (counter >= breakTime) {
            MinecraftServer.getGlobalEventHandler().call(new PlayerBlockBreakEvent(
                    player,
                    block.block(),
                    block.block(),
                    new BlockVec(block.pos()),
                    BlockFace.BOTTOM));

            this.counter = 0;
            // Handle if the player continues to break the block without a release tick
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                // Minecraft won't send a cancel digging packet if the player is still holding the block
                // but our system requires it so we simulate it here
                HypixelEventHandler.callCustomEvent(new PlayerDamageSkyBlockBlockEvent(
                        player,
                        block.pos(),
                        ClientPlayerActionPacket.Status.CANCELLED_DIGGING,
                        sequence));
                HypixelEventHandler.callCustomEvent(new PlayerDamageSkyBlockBlockEvent(
                        player,
                        block.pos(),
                        ClientPlayerActionPacket.Status.STARTED_DIGGING,
                        sequence));
            });
            return TaskSchedule.stop();
        }

        return nextSchedule;
    }

    public void cancel() {
        sendBlockBreak(-1);
        this.nextSchedule = TaskSchedule.stop();
    }

    private void sendBlockBreak(int damage) {
        int blockId = block.pos().hashCode();
        player.getPlayerConnection().sendPacket(new BlockBreakAnimationPacket(
                blockId,
                new BlockVec(block.pos()),
                (byte) damage
        ));
    }

    public record PositionedBlock(Block block, Pos pos) {
    }
}
