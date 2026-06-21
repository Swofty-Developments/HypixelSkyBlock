package net.swofty.dungeons.catacombs.party;

import net.swofty.dungeons.catacombs.CatacombsFloorDefinition;
import net.swofty.dungeons.catacombs.classes.DungeonClassType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PartyFinderService {
    private final Map<UUID, PartyFinderListing> listings = new HashMap<>();

    public PartyFinderListing create(PartyFinderMember leader, CatacombsFloorDefinition floor,
                                     int minimumCatacombsLevel, Set<DungeonClassType> allowedClasses) {
        PartyFinderListing listing = new PartyFinderListing(UUID.randomUUID(), leader, floor.floor(), floor.mode(),
                minimumCatacombsLevel, allowedClasses);
        listings.put(listing.id(), listing);
        return listing;
    }

    public PartyFinderListing listing(UUID id) {
        PartyFinderListing listing = listings.get(id);
        if (listing == null) {
            throw new IllegalArgumentException("Unknown party finder listing " + id);
        }
        return listing;
    }

    public Map<UUID, PartyFinderListing> openListings() {
        return Map.copyOf(listings.entrySet().stream()
                .filter(entry -> entry.getValue().open())
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), HashMap::putAll));
    }
}
