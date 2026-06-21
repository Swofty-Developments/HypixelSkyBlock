package net.swofty.type.skyblockgeneric.fishing.tag;

import net.swofty.type.skyblockgeneric.fishing.FishingContext;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;

public record MediumTag(FishingMedium medium) implements FishingTag {

    public static final MediumTag WATER = new MediumTag(FishingMedium.WATER);
    public static final MediumTag LAVA = new MediumTag(FishingMedium.LAVA);

    @Override
    public String id() {
        return medium.name();
    }

    @Override
    public boolean isAvailable(FishingContext context) {
        return context.medium() == medium;
    }
}
