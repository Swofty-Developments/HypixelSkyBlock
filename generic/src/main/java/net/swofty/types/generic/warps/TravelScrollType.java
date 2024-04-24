package net.swofty.types.generic.warps;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.categories.Rank;
import net.swofty.types.generic.warps.unlocks.ScrollUnlockPurchase;
import org.jetbrains.annotations.Nullable;

@Getter
public enum TravelScrollType {
    HUB_CASTLE("castle", "§bHub §7- §bCastle",
            "Spawn near the ruined castle of the past in the hub.",
            "f4559d75464b2e40a518e4de8e6cf3085f0a3ca0b1b7012614c4cd96fed60378",
            new ScrollUnlockPurchase("Lonely Philosopher"),
            new Pos(-250, 130, 45, -90, 0),
            Rank.MVP_PLUS),
    ;

    private final String internalName;
    private final String displayName;
    private final String description;
    private final String headTexture;
    private final ScrollUnlockReason unlockReason;
    private final Pos location;
    private final Rank requiredRank;

    TravelScrollType(String internalName, String displayName, String description, String headTexture, ScrollUnlockPurchase unlockReason, Pos location) {
        this.internalName = internalName;
        this.displayName = displayName;
        this.description = description;
        this.headTexture = headTexture;
        this.unlockReason = unlockReason;
        this.location = location;
        this.requiredRank = Rank.DEFAULT;
    }

    TravelScrollType(String internalName, String displayName, String description, String headTexture, ScrollUnlockPurchase unlockReason, Pos location, Rank requiredRank) {
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
