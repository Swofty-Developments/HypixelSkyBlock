package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.type.skyblockgeneric.fishing.catches.CatchPayload;

public final class FishingCatchResolver {
    private FishingCatchResolver() {
    }

    public static CatchPayload resolve(FishingContext context) {
        return FishingLootResolver.resolve(context);
    }
}
