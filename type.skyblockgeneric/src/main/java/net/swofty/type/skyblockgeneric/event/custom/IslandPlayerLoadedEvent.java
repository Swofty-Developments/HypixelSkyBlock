package net.swofty.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.Event;
import net.swofty.type.generic.user.SkyBlockIsland;
import net.swofty.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.UUID;

@Getter
public class IslandPlayerLoadedEvent implements Event {
    private final SkyBlockIsland island;
    private final HypixelPlayer player;
    private final boolean isCoop;
    private final List<HypixelPlayer> membersOnline;
    private final List<UUID> allMembers;

    public IslandPlayerLoadedEvent(SkyBlockIsland island, HypixelPlayer player, boolean isCoop, List<HypixelPlayer> membersOnline, List<UUID> allMembers) {
        this.island = island;
        this.player = player;
        this.isCoop = isCoop;
        this.membersOnline = membersOnline;
        this.allMembers = allMembers;
    }
}
