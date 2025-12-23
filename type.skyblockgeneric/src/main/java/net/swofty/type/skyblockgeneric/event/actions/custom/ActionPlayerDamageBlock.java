package net.swofty.type.skyblockgeneric.event.actions.custom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientPlayerActionPacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.type.generic.event.EventNodes;
import net.swofty.type.generic.event.HypixelEvent;
import net.swofty.type.generic.event.HypixelEventClass;
import net.swofty.type.skyblockgeneric.event.custom.PlayerDamageSkyBlockBlockEvent;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import net.swofty.type.skyblockgeneric.region.mining.BreakingTask;
import net.swofty.type.skyblockgeneric.region.mining.MineableBlock;
import net.swofty.type.skyblockgeneric.region.mining.handler.SkyBlockMiningHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerDamageBlock implements HypixelEventClass {
    public static final Map<UUID, BreakingTask> CLICKING = new HashMap<>();

    @HypixelEvent(node = EventNodes.CUSTOM, requireDataLoaded = true)
    public void run(PlayerDamageSkyBlockBlockEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(event.getBlockPosition());

        if (event.getStatus() != ClientPlayerActionPacket.Status.STARTED_DIGGING
                || region == null
                || region.getType().getMiningHandler() == null
                || Material.fromKey(player.getInstance().getBlock(event.getBlockPosition()).key()) == null
                || !region.getType().getMiningHandler().getMineableBlocks(player.getInstance(), event.getBlockPosition()).contains(
                Material.fromKey(player.getInstance().getBlock(event.getBlockPosition()).key()))
                || player.getGameMode().equals(GameMode.CREATIVE)) {
            // Cancel the task if the player is no longer breaking the block or changed block

            BreakingTask prev = CLICKING.get(player.getUuid());
            if (prev != null)
                prev.cancel();
            CLICKING.remove(player.getUuid());

            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                BlockBreakAnimationPacket breakAnim = new BlockBreakAnimationPacket(player.getEntityId(),
                        event.getBlockPosition(),
                        (byte) -1);
                player.sendPacket(breakAnim);
            }, TaskSchedule.tick(2), TaskSchedule.stop());
            return;
        }

        if (CLICKING.containsKey(player.getUuid())) {
            return;
        }

        // Check if the player's tool can break this block using the handler system
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
        MineableBlock mineableBlock = MineableBlock.get(player.getInstance().getBlock(event.getBlockPosition()));
        if (mineableBlock != null) {
            SkyBlockMiningHandler handler = mineableBlock.getMiningHandler();
            // If block doesn't break instantly and tool can't break it, return
            if (!handler.breaksInstantly() && !handler.canToolBreak(item)) {
                return;
            }
        }

        BreakingTask task = new BreakingTask(
                player,
                new BreakingTask.PositionedBlock(
                        player.getInstance().getBlock(event.getBlockPosition()),
                        event.getBlockPosition().asPos()),
                item,
                event.getSequence());
        MinecraftServer.getSchedulerManager().submitTask(task::run);
        CLICKING.put(player.getUuid(), task);
    }
}
