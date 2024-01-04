package net.swofty.event.custom;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.swofty.user.SkyBlockIsland;

import java.util.List;
import java.util.UUID;

@Getter
public class IslandFirstCreatedEvent implements Event {
    private final SkyBlockIsland island;
    private final boolean isCoop;
    private final List<UUID> allMembers;

    public IslandFirstCreatedEvent(SkyBlockIsland island, boolean isCoop, List<UUID> allMembers) {
        this.island = island;
        this.isCoop = isCoop;
        this.allMembers = allMembers;
    }
}
