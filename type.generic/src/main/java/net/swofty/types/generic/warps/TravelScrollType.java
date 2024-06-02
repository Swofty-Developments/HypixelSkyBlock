package net.swofty.types.generic.warps;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.warps.unlocks.ScrollUnlockCustomRecipe;
import net.swofty.types.generic.warps.unlocks.ScrollUnlockPurchase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public enum TravelScrollType {
    HUB_CASTLE("castle", "§bHub §7- §bCastle",
            "Spawn near the ruined castle of the past in the hub.",
            "f4559d75464b2e40a518e4de8e6cf3085f0a3ca0b1b7012614c4cd96fed60378",
            new ScrollUnlockPurchase("Lonely Philosopher"),
            new Pos(-250, 130, 45, -90, 0),
            Rank.MVP_PLUS),
    HUB_MUSEUM("museum", "§bHub §7- §bMuseum",
            "Spawn in the §9Museum §7at the foot of the §bmountain §7in the hub.",
            "f4559d75464b2e40a518e4de8e6cf3085f0a3ca0b1b7012614c4cd96fed60378",
            new ScrollUnlockCustomRecipe("Unlock this scroll's recipe through Museum Rewards."),
            new Pos(-76.5, 76, 80, -90, 0),
            Rank.DEFAULT),
    HUB_CRYPTS("crypts", "§bHub §7- §bCrypts",
            "Spawn in the §6crypts §7below the §cgraveyard §7in the hub.",
            "f4559d75464b2e40a518e4de8e6cf3085f0a3ca0b1b7012614c4cd96fed60378",
            new ScrollUnlockCustomRecipe("Unlock this scroll's recipe through Recipe Unlock."),
            new Pos(-190, 74, -89, -90, 0),
            Rank.MVP_PLUS),
    ;

    private final @NotNull String internalName;
    private final @NotNull String displayName;
    private final @NotNull String description;
    private final @NotNull String headTexture;
    private final @NotNull ScrollUnlockReason unlockReason;
    private final @NotNull Pos location;
    private final @NotNull Rank requiredRank;

    TravelScrollType(@NotNull String internalName, @NotNull String displayName, @NotNull String description,
                     @NotNull String headTexture, @NotNull ScrollUnlockReason unlockReason, @NotNull Pos location) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.description = description;
        this.headTexture = headTexture;
        this.unlockReason = unlockReason;
        this.location = location;
        this.requiredRank = Rank.DEFAULT;
    }

    TravelScrollType(@NotNull String internalName, @NotNull String displayName, @NotNull String description,
                     @NotNull String headTexture, @NotNull ScrollUnlockReason unlockReason, @NotNull Pos location,
                     @NotNull Rank requiredRank) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.description = description;
        this.headTexture = headTexture;
        this.unlockReason = unlockReason;
        this.location = location;
        this.requiredRank = requiredRank;
    }

    public static @Nullable TravelScrollType getFromInternalName(String internalName) {
        for (TravelScrollType scroll : values()) {
            if (scroll.getInternalName().equalsIgnoreCase(internalName)) {
                return scroll;
            }
        }
        return null;
    }
}
