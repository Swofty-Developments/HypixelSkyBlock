package net.swofty.type.skyblockgeneric.fishing.tag;

import net.swofty.type.skyblockgeneric.fishing.FishingContext;

/**
 * A trait attached to a sea creature, trophy fish, or fishing-rod component
 * that the loot resolver can reason about (gating, tag-bonus stacking, etc.).
 *
 * The sealed hierarchy carries gating logic on each implementation so the
 * resolver never has to look at string identifiers to decide whether a tag's
 * condition holds — that's the implementor's job.
 */
public sealed interface FishingTag
    permits MediumTag, TimeOfDayTag, WeatherTag, RegionTag, RarityTag, EventTag {

    String id();

    /**
     * @return true if this tag's environmental condition currently holds.
     *         Informational tags (regional, rarity) always return true.
     */
    default boolean isAvailable(FishingContext context) {
        return true;
    }
}
