package net.swofty.types.generic.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.event.SkyBlockEvent;
import net.swofty.types.generic.region.SkyBlockRegion;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionPlayerStrayTooFar extends SkyBlockEvent {
    public static Map<UUID, Long> startedStray = new HashMap<>();

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event tempEvent) {
        PlayerMoveEvent event = (PlayerMoveEvent) tempEvent;
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockRegion region = player.getRegion();

        if (region != null) {
            startedStray.remove(player.getUuid());
            return;
        }

        if (startedStray.containsKey(player.getUuid())) {
            if (System.currentTimeMillis() - startedStray.get(player.getUuid()) > 5000) {
                player.teleport(SkyBlockConst.getTypeLoader().getLoaderValues().spawnPosition().apply(
                        player.getOriginServer()
                ));
                startedStray.remove(player.getUuid());
                player.sendMessage("§cYou have strayed too far from the spawn! Teleporting you back...");
            }
        } else {
            startedStray.put(player.getUuid(), System.currentTimeMillis());
        }
    }
}
