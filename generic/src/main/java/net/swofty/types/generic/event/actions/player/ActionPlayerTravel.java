package net.swofty.types.generic.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.mission.MissionData;
import net.swofty.types.generic.mission.MissionSet;
import net.swofty.types.generic.mission.missions.MissionUseTeleporter;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.event.EventNodes;
import net.swofty.types.generic.event.EventParameters;
import net.swofty.types.generic.event.SkyBlockEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventParameters(description = "Handles travelling between the Island and Hub world",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerTravel extends SkyBlockEvent {
    public static List<UUID> delay = new ArrayList<>();

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerMoveEvent.getPlayer();

        if (delay.contains(player.getUuid())) return;

        NamespaceID block = player.getInstance().getBlock(player.getPosition()).namespace();

        if (block == Block.NETHER_PORTAL.namespace()) {
            MissionData data = player.getMissionData();

            if (!MissionSet.GETTING_STARTED.hasCompleted(player) && !data.isCurrentlyActive(MissionUseTeleporter.class)) {
                player.sendMessage("§cYou must complete your starting missions!");
                return;
            }

            player.sendTo(ServerType.VILLAGE);
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }

        if (block == Block.END_PORTAL.namespace()) {
            player.sendTo(ServerType.ISLAND);
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }
    }
}
