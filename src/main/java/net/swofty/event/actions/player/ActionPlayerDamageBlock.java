package net.swofty.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientPlayerDiggingPacket;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.SkyBlock;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.item.SkyBlockItem;
import net.swofty.mining.BreakingTask;
import net.swofty.mining.MineableBlock;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.utility.PositionedBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventParameters(
        description = "Damage block",
        node = EventNodes.PLAYER,
        requireDataLoaded = true
)
public class ActionPlayerDamageBlock extends SkyBlockEvent
{
      public static final Map<UUID, BreakingTask> CLICKING = new HashMap<>();

      @Override
      public Class<? extends Event> getEvent() {
            return PlayerPacketEvent.class;
      }

      @Override
      public void run(Event event) {
            PlayerPacketEvent e = (PlayerPacketEvent) event;
            if (!(e.getPacket() instanceof ClientPlayerDiggingPacket packet)) return;
            SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();

            BreakingTask prev = CLICKING.get(player.getUuid());
            if (prev != null)
                  prev.cancel();

            BreakingTask task = new BreakingTask(player, new PositionedBlock(SkyBlock.getInstanceContainer().getBlock(packet.blockPosition()), Pos.fromPoint(packet.blockPosition())),
                    new SkyBlockItem(player.getItemInMainHand()));
            MinecraftServer.getSchedulerManager().submitTask(task::run);
            CLICKING.put(player.getUuid(), task);
      }
}
