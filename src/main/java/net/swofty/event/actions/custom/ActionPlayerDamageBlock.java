package net.swofty.event.actions.custom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.custom.PlayerDamageSkyBlockBlock;
import net.swofty.item.SkyBlockItem;
import net.swofty.region.SkyBlockRegion;
import net.swofty.region.mining.BreakingTask;
import net.swofty.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventParameters(description = "Damage block",
        node = EventNodes.CUSTOM,
        requireDataLoaded = true)
public class ActionPlayerDamageBlock extends SkyBlockEvent {
      public static final Map<UUID, BreakingTask> CLICKING = new HashMap<>();

      @Override
      public Class<? extends Event> getEvent() {
            return PlayerDamageSkyBlockBlock.class;
      }

      @Override
      public void run(Event event) {
            PlayerDamageSkyBlockBlock e = (PlayerDamageSkyBlockBlock) event;
            SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
            SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(e.getBlockPosition());


            if (e.getStatus() != ClientPlayerDiggingPacket.Status.STARTED_DIGGING
                    || region == null
                    || region.getType().getMiningHandler() == null
                    || !region.getType().getMiningHandler().getMineableBlocks().contains(
                            Material.fromNamespaceId(player.getInstance().getBlock(e.getBlockPosition()).namespace()))
                    || player.getGameMode().equals(GameMode.CREATIVE)) {

                  BreakingTask prev = CLICKING.get(player.getUuid());
                  if (prev != null)
                        prev.cancel();
                  CLICKING.remove(player.getUuid());

                  MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                        BlockBreakAnimationPacket breakAnim = new BlockBreakAnimationPacket(player.getEntityId(),
                                e.getBlockPosition(),
                                (byte) -1);
                        player.sendPacket(breakAnim);
                  }, TaskSchedule.tick(2), TaskSchedule.stop());
                  return;
            }

            if (CLICKING.containsKey(player.getUuid())) {
                  return;
            }

            // Ensure that the player isn't just using their hand
            SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
            if (!item.getAttributeHandler().isMiningTool()) {
                  return;
            }

            System.out.println(player.getInstance());

            BreakingTask task = new BreakingTask(
                    player,
                    new BreakingTask.PositionedBlock(
                            player.getInstance().getBlock(e.getBlockPosition()),
                            Pos.fromPoint(e.getBlockPosition())),
                    item);
            MinecraftServer.getSchedulerManager().submitTask(task::run);
            CLICKING.put(player.getUuid(), task);
      }
}
