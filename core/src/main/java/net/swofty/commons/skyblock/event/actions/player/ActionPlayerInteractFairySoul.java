package net.swofty.commons.skyblock.event.actions.player;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.user.fairysouls.EntityFairySoul;
import net.swofty.commons.skyblock.user.fairysouls.FairySoul;

@EventParameters(description = "Checks to see if a player clicks on a Fairy Soul",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class ActionPlayerInteractFairySoul extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerEntityInteractEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerEntityInteractEvent playerEvent = (PlayerEntityInteractEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();

        if (playerEvent.getTarget() instanceof EntityFairySoul entitySoul) {
            FairySoul fairySoul = entitySoul.parent;
            if (fairySoul == null) return;

            fairySoul.collect(player);
        }
    }
}
