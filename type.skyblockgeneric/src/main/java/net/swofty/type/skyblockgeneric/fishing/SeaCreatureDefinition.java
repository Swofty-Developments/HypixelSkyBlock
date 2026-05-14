package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.skyblockgeneric.fishing.tags.FishingTag;

import java.util.List;

public record SeaCreatureDefinition(
    String id,
    int requiredFishingLevel,
    double skillXp,
    List<FishingTag> tags
) {
    public boolean hasTag(FishingTag tag) {
        return tags.contains(tag);
    }

    public boolean isAvailable(FishingContext context) {
        for (FishingTag tag : tags) {
            if (!tag.isAvailable(context)) {
                return false;
            }
        }
        return true;
    }
}
