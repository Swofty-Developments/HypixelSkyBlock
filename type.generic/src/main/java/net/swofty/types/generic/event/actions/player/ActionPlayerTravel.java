package net.swofty.types.generic.event.actions.player;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.SkyBlockEventClass;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.mission.missions.MissionUseTeleporter;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.SkyBlockEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionPlayerTravel implements SkyBlockEventClass {
    public static List<UUID> delay = new ArrayList<>();

    @SkyBlockEvent(node = EventNodes.PLAYER , requireDataLoaded = true)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (delay.contains(player.getUuid())) return;

        Key block = player.getInstance().getBlock(player.getPosition()).key();

        if (block == Block.NETHER_PORTAL.key()) {
            MissionData data = player.getMissionData();

            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();

            if (!MissionSet.GETTING_STARTED.hasCompleted(player)
                    && !data.isCurrentlyActive(MissionUseTeleporter.class)
            ) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }

            player.sendTo(SkyBlockConst.getTypeLoader().getType() == ServerType.HUB ? ServerType.DUNGEON_HUB : ServerType.HUB);
        }

        if (block == Block.END_PORTAL.key()) {
            player.sendTo(ServerType.ISLAND);
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }
    }
}
