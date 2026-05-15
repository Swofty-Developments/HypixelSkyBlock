package net.swofty.type.skyblockgeneric.fishing.resolver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.swofty.type.skyblockgeneric.fishing.FishingContext;
import net.swofty.type.skyblockgeneric.fishing.catches.CatchPayload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingCatchResolver {

    public static CatchPayload resolve(FishingContext context) {
        return FishingLootResolver.resolve(context);
    }
}
