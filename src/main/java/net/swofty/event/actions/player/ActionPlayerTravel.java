package net.swofty.event.actions.player;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import net.swofty.entity.DroppedItemEntityImpl;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import net.swofty.event.actions.player.fall.ActionPlayerFall;
import net.swofty.user.SkyBlockPlayer;
import org.tinylog.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventParameters(description = "Handles travelling between the Island and Hub world",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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
            player.sendToHub();
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }

        if (block == Block.END_PORTAL.namespace()) {
            player.sendToIsland();
            delay.add(player.getUuid());

            MinecraftServer.getSchedulerManager().buildTask(() -> delay.remove(player.getUuid()))
                    .delay(Duration.ofMillis(500))
                    .schedule();
        }
    }
}
