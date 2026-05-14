package net.swofty.type.skyblockgeneric.fishing.resolver;

import net.swofty.type.skyblockgeneric.fishing.FishingContext;
import net.swofty.type.skyblockgeneric.fishing.catches.CatchPayload;

public final class FishingCatchResolver {
    private FishingCatchResolver() {
    }

    public static CatchPayload resolve(FishingContext context) {
        return FishingLootResolver.resolve(context);
    }
}
