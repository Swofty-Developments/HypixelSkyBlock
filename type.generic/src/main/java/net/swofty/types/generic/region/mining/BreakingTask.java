package net.swofty.types.generic.region.mining;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class BreakingTask {
    private int counter = 0;
    private final SkyBlockPlayer player;
    private final PositionedBlock block;
    private final double breakTime;
    private TaskSchedule nextSchedule;

    public BreakingTask(SkyBlockPlayer player, PositionedBlock block, SkyBlockItem item) {
        this.player = player;
        this.block = block;

        double miningTime = player.getTimeToMine(item, block.block());
        if (miningTime < 1 && miningTime != -1) {
            miningTime = 1;
        }

        this.breakTime = miningTime;
        this.nextSchedule = TaskSchedule.tick(1);
    }

    public TaskSchedule run() {
        if (nextSchedule == TaskSchedule.stop()) {
            return TaskSchedule.stop();
        }

        int dmg = (int) ((counter / (float) breakTime) * 10);
        sendBlockBreak(dmg);
        counter += 1;

        if (counter >= breakTime) {
            MinecraftServer.getGlobalEventHandler().call(new PlayerBlockBreakEvent(
                    player,
                    block.block(),
                    block.block(),
                    block.pos(),
                    BlockFace.BOTTOM));

            this.counter = 0;
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                SkyBlockEventHandler.callSkyBlockEvent(new PlayerDamageSkyBlockBlockEvent(
                        player,
                        block.pos(),
                        ClientPlayerDiggingPacket.Status.CANCELLED_DIGGING));
                SkyBlockEventHandler.callSkyBlockEvent(new PlayerDamageSkyBlockBlockEvent(
                        player,
                        block.pos(),
                        ClientPlayerDiggingPacket.Status.STARTED_DIGGING));
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
        BlockBreakAnimationPacket breakAnim = new BlockBreakAnimationPacket(player.getEntityId() + 1, block.pos(), (byte) damage);
        player.getPlayerConnection().sendPackets(breakAnim);
    }

    public record PositionedBlock(Block block, Pos pos) {
    }
}
