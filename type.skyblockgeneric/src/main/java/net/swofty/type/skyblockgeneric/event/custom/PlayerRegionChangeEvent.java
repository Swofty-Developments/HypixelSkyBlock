package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.swofty.type.generic.region.RegionType;
import net.swofty.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.NotNull;

public class PlayerRegionChangeEvent implements PlayerInstanceEvent {
    private final HypixelPlayer player;
    @Getter
    private final RegionType from;
    @Getter
    private final RegionType to;

    public PlayerRegionChangeEvent(HypixelPlayer player, RegionType from, RegionType to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    @Override
    public @NotNull HypixelPlayer getPlayer() {
        return player;
    }
}
