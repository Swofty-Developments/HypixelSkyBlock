package net.swofty.type.skyblockgeneric.fishing;

public final class FishingCatchResolver {
    private FishingCatchResolver() {
    }

    public static FishingCatchResult resolve(FishingContext context) {
        return FishingLootResolver.resolve(context);
    }
}
