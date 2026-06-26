package net.swofty.type.skyblockgeneric.wardrobe;

import net.minestom.server.item.Material;
import net.swofty.type.generic.user.categories.Rank;
import net.swofty.type.skyblockgeneric.data.datapoints.DatapointWardrobe;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

public final class WardrobeService {

    public static int rankSlots(Rank rank) {
        if (rank.isEqualOrHigherThan(Rank.MVP_PLUS)) return 18;
        if (rank.isEqualOrHigherThan(Rank.MVP)) return 13;
        if (rank.isEqualOrHigherThan(Rank.VIP_PLUS)) return 9;
        if (rank.isEqualOrHigherThan(Rank.VIP)) return 5;
        return 2;
    }

    public static boolean isUnlocked(int slot, Rank rank, DatapointWardrobe.WardrobeData data) {
        return slot < Math.min(27, rankSlots(rank) + data.getCommunitySlots());
    }

    public static boolean accepts(int pieceIndex, SkyBlockItem item) {
        if (item == null || item.isNA()) return true;
        Material material = item.getMaterial();
        String key = material.key().value();
        return switch (pieceIndex) {
            case 0 -> key.endsWith("_helmet") || material == Material.PLAYER_HEAD;
            case 1 -> key.endsWith("_chestplate") || material == Material.ELYTRA;
            case 2 -> key.endsWith("_leggings");
            case 3 -> key.endsWith("_boots");
            default -> false;
        };
    }
}
