package net.swofty.commons.skyblock.event.actions.player.npc;

import net.minestom.server.event.Event;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.swofty.commons.skyblock.entity.villager.SkyBlockVillagerNPC;
import net.swofty.commons.skyblock.event.EventNodes;
import net.swofty.commons.skyblock.event.EventParameters;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

@EventParameters(description = "Handles seeing if Villagers are in range of a player",
        node = EventNodes.PLAYER,
        validLocations = EventParameters.Location.HUB,
        requireDataLoaded = true)
public class ActionPlayerMoveVillagerNPC extends SkyBlockEvent {
    @Override
    public Class<? extends Event> getEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public void run(Event event) {
        PlayerMoveEvent playerEvent = (PlayerMoveEvent) event;
        final SkyBlockPlayer player = (SkyBlockPlayer) playerEvent.getPlayer();
        SkyBlockVillagerNPC.updateForPlayer(player);
    }
}
