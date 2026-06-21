package net.swofty.type.skyblockgeneric.fishing.tag;

import java.util.Set;
import net.swofty.type.skyblockgeneric.fishing.FishingContext;

/**
 * Marks loot that's themed to one or more SkyBlock regions. Acts as a
 * gate when {@code requireMatch} is true, otherwise informational only.
 */
public record RegionTag(String id, Set<String> regionIds, boolean requireMatch) implements FishingTag {

    public static final RegionTag CRIMSON_ISLE = require("CRIMSON", Set.of("CRIMSON_ISLE", "DRAGONTAIL", "STRONGHOLD"));
    public static final RegionTag BACKWATER_BAYOU = require("BAYOU", Set.of("BACKWATER_BAYOU"));
    public static final RegionTag GALATEA = require("GALATEA", Set.of("GALATEA"));
    public static final RegionTag PRIVATE_ISLAND = require("ISLAND", Set.of("PRIVATE_ISLAND"));
    public static final RegionTag HUB = require("HUB", Set.of("HUB"));
    public static final RegionTag THE_END = require("END", Set.of("THE_END", "DRAGONS_NEST"));

    public static RegionTag require(String id, Set<String> regionIds) {
        return new RegionTag(id, Set.copyOf(regionIds), true);
    }

    public static RegionTag info(String id) {
        return new RegionTag(id, Set.of(), false);
    }

    @Override
    public boolean isAvailable(FishingContext context) {
        if (!requireMatch || regionIds.isEmpty()) {
            return true;
        }
        return context.regionId() != null && regionIds.contains(context.regionId());
    }
}
