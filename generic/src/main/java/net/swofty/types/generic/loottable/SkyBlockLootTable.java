package net.swofty.types.generic.loottable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class SkyBlockLootTable {
    public abstract @NonNull List<LootRecord> getLootTable();

    public List<ItemType> getLootTableItems() {
        List<ItemType> items = new java.util.ArrayList<>();
        for (LootRecord record : getLootTable()) {
            items.add(record.itemType);
        }
        return items;
    }

    /**
     * Function will always return a LootRecord for every itemtype
     * @param player The player to run the chances for
     * @return A map of the loot that the player will receive
     */
    public @NonNull Map<ItemType, LootRecord> runChances(SkyBlockPlayer player, LootAffector... affectors) {
        Map<ItemType, LootRecord> loot = new HashMap<>();

        for (LootRecord record : getLootTable()) {
            if (record.shouldCalculate.apply(player)) {
                double chance = record.chance;
                for (LootAffector affector : affectors) {
                    chance += affector.getAffector().apply(player, chance);
                }

                if (Math.random() * 100 <= record.chance) {
                    loot.put(record.itemType, record);
                } else {
                    loot.put(record.itemType, LootRecord.none(0));
                }
            }
        }

        return loot;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LootRecord {
        private final ItemType itemType;
        private final int amount;
        private final double chance;
        private final Function<SkyBlockPlayer, Boolean> shouldCalculate;

        public static LootRecord none(int chance) {
            return new LootRecord(ItemType.AIR, 0, chance, player -> true);
        }

        public static LootRecord none(int chance, Function<SkyBlockPlayer, Boolean> shouldCalculate) {
            return new LootRecord(ItemType.AIR, 0, chance, shouldCalculate);
        }

        public static boolean isNone(LootRecord lootRecord) {
            return lootRecord.itemType == ItemType.AIR && lootRecord.amount == 0;
        }
    }
}
