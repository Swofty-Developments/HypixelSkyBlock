package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.event.Event;
import net.swofty.user.SkyBlockIsland;
import net.swofty.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;

@Getter
public class IslandFetchedFromDatabaseEvent implements Event {
    private final SkyBlockIsland island;
    private final boolean isCoop;
    private final List<SkyBlockPlayer> membersOnline;
    private final List<UUID> allMembers;

    public IslandFetchedFromDatabaseEvent(SkyBlockIsland island, boolean isCoop, List<SkyBlockPlayer> membersOnline, List<UUID> allMembers) {
        this.island = island;
        this.isCoop = isCoop;
        this.membersOnline = membersOnline;
        this.allMembers = allMembers;
    }
}
