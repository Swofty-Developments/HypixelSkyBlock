package net.swofty.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;

@EventParameters(description = "Sets the players skin when init",
        node = EventNodes.PLAYER,
        requireDataLoaded = true)
public class ActionPlayerSkinInit extends SkyBlockEvent {

    @Override
    public Class<? extends Event> getEvent() {
        return PlayerSkinInitEvent.class;
    }

    @SneakyThrows
    @Override
    public void run(Event event) {
        PlayerSkinInitEvent playerSkinInitEvent = (PlayerSkinInitEvent) event;
        final Player player = playerSkinInitEvent.getPlayer();

        playerSkinInitEvent.setSkin(PlayerSkin.fromUsername(player.getUsername().replace("cracked", "")));
    }
}
