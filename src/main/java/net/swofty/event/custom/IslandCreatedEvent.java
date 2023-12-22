package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;

@Getter
public class IslandCreatedEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkyBlockIsland island;

    public IslandCreatedEvent(SkyBlockPlayer player, SkyBlockIsland island) {
        this.player = player;
        this.island = island;
    }
}
