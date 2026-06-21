package net.swofty.dungeons.catacombs.party;

import net.swofty.dungeons.catacombs.CatacombsFloor;
import net.swofty.dungeons.catacombs.CatacombsMode;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PartyFinderListing {
    private final UUID id;
    private final UUID leaderId;
    private final CatacombsFloor floor;
    private final CatacombsMode mode;
    private final int minimumCatacombsLevel;
    private final Set<DungeonClassType> allowedClasses;
    private final Map<UUID, PartyFinderMember> members = new LinkedHashMap<>();
    private boolean open = true;

    public PartyFinderListing(UUID id, PartyFinderMember leader, CatacombsFloor floor, CatacombsMode mode,
                              int minimumCatacombsLevel, Set<DungeonClassType> allowedClasses) {
        this.id = id;
        this.leaderId = leader.playerId();
        this.floor = floor;
        this.mode = mode;
        this.minimumCatacombsLevel = minimumCatacombsLevel;
        this.allowedClasses = allowedClasses.isEmpty()
                ? EnumSet.allOf(DungeonClassType.class)
                : EnumSet.copyOf(allowedClasses);
        members.put(leader.playerId(), leader);
    }

    public UUID id() {
        return id;
    }

    public UUID leaderId() {
        return leaderId;
    }

    public CatacombsFloor floor() {
        return floor;
    }

    public CatacombsMode mode() {
        return mode;
    }

    public int minimumCatacombsLevel() {
        return minimumCatacombsLevel;
    }

    public boolean open() {
        return open;
    }

    public Map<UUID, PartyFinderMember> members() {
        return Map.copyOf(members);
    }

    public boolean full() {
        return members.size() >= 5;
    }

    public boolean ready() {
        return members.size() >= 2 && members.values().stream().allMatch(PartyFinderMember::ready);
    }

    public void join(PartyFinderMember member, int catacombsLevel) {
        if (!open) {
            throw new IllegalStateException("Party finder listing is closed");
        }
        if (full()) {
            throw new IllegalStateException("Party finder listing is full");
        }
        if (catacombsLevel < minimumCatacombsLevel) {
            throw new IllegalArgumentException("Catacombs level " + catacombsLevel + " is below " + minimumCatacombsLevel);
        }
        if (member.selectedClass() != null && !allowedClasses.contains(member.selectedClass())) {
            throw new IllegalArgumentException(member.selectedClass() + " is not allowed in this listing");
        }
        members.put(member.playerId(), member);
    }

    public void updateClass(UUID playerId, DungeonClassType type) {
        PartyFinderMember member = member(playerId);
        if (!allowedClasses.contains(type)) {
            throw new IllegalArgumentException(type + " is not allowed in this listing");
        }
        members.put(playerId, member.withClass(type));
    }

    public void ready(UUID playerId, boolean ready) {
        members.put(playerId, member(playerId).withReady(ready));
    }

    public void close() {
        open = false;
    }

    private PartyFinderMember member(UUID playerId) {
        PartyFinderMember member = members.get(playerId);
        if (member == null) {
            throw new IllegalArgumentException("Player is not in this party finder listing");
        }
        return member;
    }
}
