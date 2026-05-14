package net.swofty.type.skyblockgeneric.fishing;

import java.util.List;

public record FishingTableDefinition(
    String id,
    List<String> regions,
    List<FishingMedium> mediums,
    List<LootEntry> items,
    List<LootEntry> treasures,
    List<LootEntry> junk,
    List<SeaCreatureRoll> seaCreatures
) {
    public record LootEntry(String itemId, double chance, int amount, double skillXp) {
    }

    public record SeaCreatureRoll(String seaCreatureId, double chance) {
    }
}
