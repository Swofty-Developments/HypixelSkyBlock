package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.region.SkyBlockRegion;
import net.swofty.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerRegionChange implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final SkyBlockRegion from;
    @Getter
    private final SkyBlockRegion to;

    public PlayerRegionChange(SkyBlockPlayer player, SkyBlockRegion from, SkyBlockRegion to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
        return player;
    }
}
