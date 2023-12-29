package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.region.RegionType;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class IslandLoadedEvent implements Event {
    private final SkyBlockPlayer player;
    private final SkyBlockIsland island;

    public IslandLoadedEvent(SkyBlockPlayer player, SkyBlockIsland island) {
        this.player = player;
        this.island = island;
    }
}
