package net.swofty.type.skyblockgeneric.fishing.registry;

import java.util.List;
import net.swofty.type.skyblockgeneric.fishing.FishingContext;
import net.swofty.type.skyblockgeneric.fishing.tag.FishingTag;

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
