package net.swofty.dungeons.catacombs.flow;

import net.swofty.dungeons.catacombs.CatacombsFloor;
import net.swofty.dungeons.catacombs.CatacombsFloorDefinition;
import net.swofty.dungeons.catacombs.CatacombsMode;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;
import net.swofty.dungeons.catacombs.party.PartyFinderListing;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CatacombsJoinSession {
    private final UUID playerId;
    private final List<CatacombsJoinTrace> traces = new ArrayList<>();
    private CatacombsMode mode;
    private CatacombsFloor floor;
    private DungeonClassType selectedClass;
    private String selectedKitId;
    private PartyFinderListing listing;

    private CatacombsJoinSession(UUID playerId) {
        this.playerId = playerId;
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.CLICK_MORT, "Mort opened Catacombs menu"));
    }

    public static CatacombsJoinSession clickedMort(UUID playerId) {
        return new CatacombsJoinSession(playerId);
    }

    public UUID playerId() {
        return playerId;
    }

    public CatacombsMode mode() {
        return mode;
    }

    public CatacombsFloor floor() {
        return floor;
    }

    public DungeonClassType selectedClass() {
        return selectedClass;
    }

    public String selectedKitId() {
        return selectedKitId;
    }

    public PartyFinderListing listing() {
        return listing;
    }

    public List<CatacombsJoinTrace> traces() {
        return List.copyOf(traces);
    }

    public CatacombsJoinSession selectMode(CatacombsMode mode) {
        this.mode = mode;
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.SELECT_MODE, mode.name()));
        return this;
    }

    public CatacombsJoinSession selectFloor(CatacombsFloorDefinition definition) {
        this.floor = definition.floor();
        this.mode = definition.mode();
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.SELECT_FLOOR, definition.floor().displayName()));
        return this;
    }

    public CatacombsJoinSession joinPartyFinder(PartyFinderListing listing) {
        this.listing = listing;
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.PARTY_FINDER, listing.id().toString()));
        return this;
    }

    public CatacombsJoinSession readyCheck() {
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.READY_CHECK, "Player ready"));
        return this;
    }

    public CatacombsJoinSession selectClass(DungeonClassType type) {
        this.selectedClass = type;
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.CLASS_SELECTION, type.displayName()));
        return this;
    }

    public CatacombsJoinSession selectKit(String kitId) {
        this.selectedKitId = kitId;
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.KIT_SELECTION, kitId));
        return this;
    }

    public CatacombsJoinSession instanceCreated() {
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.INSTANCE_CREATE, "Dungeon instance allocated"));
        return this;
    }

    public CatacombsJoinSession startRoom() {
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.START_ROOM, "Players spawned in start room"));
        return this;
    }

    public CatacombsJoinSession runStarted() {
        traces.add(CatacombsJoinTrace.of(CatacombsJoinStep.RUN_STARTED, "Blood door locked, run timer started"));
        return this;
    }
}
