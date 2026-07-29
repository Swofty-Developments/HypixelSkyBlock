package net.swofty.dungeons.catacombs.party;

import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.UUID;

public record PartyFinderMember(
        UUID playerId,
        String name,
        PartyFinderRole role,
        DungeonClassType selectedClass,
        boolean ready
) {
    public PartyFinderMember withClass(DungeonClassType selectedClass) {
        return new PartyFinderMember(playerId, name, role, selectedClass, ready);
    }

    public PartyFinderMember withReady(boolean ready) {
        return new PartyFinderMember(playerId, name, role, selectedClass, ready);
    }
}
