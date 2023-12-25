package net.swofty.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerSkinInitEvent;
import net.swofty.event.EventNodes;
import net.swofty.event.EventParameters;
import net.swofty.event.SkyBlockEvent;
import org.tinylog.Logger;

@EventParameters(description = "Sets the players skin when init",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.EITHER,
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

        PlayerSkin skin = PlayerSkin.fromUsername(player.getUsername().replace("cracked", ""));
        playerSkinInitEvent.setSkin(skin);
    }
}
