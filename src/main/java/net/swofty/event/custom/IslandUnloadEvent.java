package net.swofty.event.custom;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class IslandUnloadEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    private final SkyBlockIsland island;

    public IslandUnloadEvent(SkyBlockPlayer player, SkyBlockIsland island) {
        this.player = player;
        this.island = island;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
