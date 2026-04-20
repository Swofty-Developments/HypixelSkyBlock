package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FishingLootResolver {
    private FishingLootResolver() {
    }

    public static FishingCatchResult resolve(FishingContext context) {
        FishingCatchResult questCatch = tryResolveQuestCatch(context);
        if (questCatch != null) {
            return questCatch;
        }

        FishingCatchResult trophyFish = tryResolveTrophyFish(context);
        if (trophyFish != null) {
            return trophyFish;
        }

        FishingCatchResult seaCreature = tryResolveSeaCreature(context);
        if (seaCreature != null) {
            return seaCreature;
        }

        return resolveItem(context);
    }

    private static FishingCatchResult tryResolveTrophyFish(FishingContext context) {
        if (context.medium() != FishingMedium.LAVA) {
            return null;
        }

        double bonus = getTotalStatistic(context, ItemStatistic.TROPHY_FISH_CHANCE);
        if (context.bait() != null) {
            bonus += context.bait().getTrophyFishChanceBonus();
        }

        List<TrophyFishDefinition> eligible = new ArrayList<>();
        for (TrophyFishDefinition definition : FishingRegistry.getTrophyFish()) {
            if (!definition.regions().isEmpty() && context.regionId() != null && !definition.regions().contains(context.regionId())) {
                continue;
            }
            if (context.player().getSkills().getCurrentLevel(net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING) < definition.requiredFishingLevel()) {
                continue;
            }
            if (context.castDurationMs() < definition.minimumCastTimeMs()) {
                continue;
            }
            eligible.add(definition);
        }

        eligible.sort(Comparator.comparingDouble(TrophyFishDefinition::catchChance));
        for (TrophyFishDefinition definition : eligible) {
            if (Math.random() * 100 <= definition.catchChance() + bonus) {
                String tier = rollTrophyTier(context, definition);
                String itemId = switch (tier) {
                    case "DIAMOND" -> definition.diamondItemId();
                    case "GOLD" -> definition.goldItemId();
                    case "SILVER" -> definition.silverItemId();
                    default -> definition.bronzeItemId();
                };
                if (itemId == null) {
                    continue;
                }
                return new FishingCatchResult(FishingCatchKind.TROPHY_FISH, itemId, null, definition.id(), 1, 300.0D, false, null);
            }
        }

        return null;
    }

    private static FishingCatchResult tryResolveQuestCatch(FishingContext context) {
        if (context.medium() != FishingMedium.WATER) {
            return null;
        }
        if (context.regionId() == null) {
            return null;
        }
        if (!"FISHING_OUTPOST".equals(context.regionId()) && !"FISHERMANS_HUT".equals(context.regionId())) {
            return null;
        }
        if (!context.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISHERWOMAN_ENID)) {
            return null;
        }
        if (context.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_UNLOCKED_SHIP)) {
            return null;
        }
        if (context.player().getShipState().getEngine() != null) {
            return null;
        }
        if (context.player().countItem(ItemType.RUSTY_SHIP_ENGINE) > 0) {
            return null;
        }
        if (Math.random() * 100 > 1.0D) {
            return null;
        }

        return new FishingCatchResult(
            FishingCatchKind.QUEST,
            ItemType.RUSTY_SHIP_ENGINE.name(),
            null,
            null,
            1,
            15.0D,
            false,
            "You fished up a Rusty Ship Engine!"
        );
    }

    private static String rollTrophyTier(FishingContext context, TrophyFishDefinition definition) {
        var progress = context.player().getTrophyFishData().getProgress(definition.id());
        if (progress.getTotalCatches() + 1 >= 600 && !progress.hasTier("DIAMOND")) {
            return "DIAMOND";
        }
        if (progress.getTotalCatches() + 1 >= 100 && !progress.hasTier("GOLD")) {
            return "GOLD";
        }

        double charmBonus = 0.0D;
        var charm = context.rod().getAttributeHandler().getEnchantment(net.swofty.type.skyblockgeneric.enchantment.EnchantmentType.CHARM);
        if (charm != null) {
            charmBonus = charm.level() * 2.0D;
        }

        if (Math.random() <= (0.002D * (1 + charmBonus / 100D))) {
            return "DIAMOND";
        }
        if (Math.random() <= (0.02D * (1 + charmBonus / 100D))) {
            return "GOLD";
        }
        if (Math.random() <= (0.25D * (1 + charmBonus / 100D))) {
            return "SILVER";
        }
        return "BRONZE";
    }

    private static FishingCatchResult tryResolveSeaCreature(FishingContext context) {
        FishingTableDefinition table = findTable(context);
        if (table == null) {
            return null;
        }

        double seaCreatureChance = getTotalStatistic(context, ItemStatistic.SEA_CREATURE_CHANCE);
        if (context.hook() != null && context.hook().isTreasureOnly()) {
            return null;
        }

        for (FishingTableDefinition.SeaCreatureRoll roll : table.seaCreatures()) {
            SeaCreatureDefinition definition = FishingRegistry.getSeaCreature(roll.seaCreatureId());
            if (definition != null && context.player().getSkills().getCurrentLevel(net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING) < definition.requiredFishingLevel()) {
                continue;
            }
            double tagBonus = definition == null ? 0.0D : getTagBonus(context, definition.tags());
            if (Math.random() * 100 <= roll.chance() + seaCreatureChance + tagBonus) {
                double skillXp = definition == null ? 0.0D : definition.skillXp();
                return new FishingCatchResult(FishingCatchKind.SEA_CREATURE, null, roll.seaCreatureId(), null, 1, skillXp, false, null);
            }
        }
        return null;
    }

    private static FishingCatchResult resolveItem(FishingContext context) {
        FishingTableDefinition table = findTable(context);
        if (table == null) {
            return new FishingCatchResult(FishingCatchKind.ITEM, "RAW_FISH", null, null, 1, 5.0D, false, null);
        }

        List<FishingTableDefinition.LootEntry> pool = table.items();
        double treasureChance = getTotalStatistic(context, ItemStatistic.TREASURE_CHANCE);
        if (context.bait() != null) {
            treasureChance += context.bait().getTreasureChanceBonus();
        }
        if (context.sinker() != null && context.sinker().isBayouTreasureToJunk()) {
            treasureChance += 10.0D;
        }

        if (!table.treasures().isEmpty() && Math.random() * 100 <= treasureChance) {
            pool = context.sinker() != null && context.sinker().isBayouTreasureToJunk() ? table.junk() : table.treasures();
            return pick(pool, FishingCatchKind.TREASURE);
        }

        FishingCatchResult result = pick(pool, FishingCatchKind.ITEM);
        if (result != null) {
            return result;
        }
        if (!table.junk().isEmpty()) {
            return pick(table.junk(), FishingCatchKind.ITEM);
        }
        return new FishingCatchResult(FishingCatchKind.ITEM, "RAW_FISH", null, null, 1, 5.0D, false, null);
    }

    private static FishingCatchResult pick(List<FishingTableDefinition.LootEntry> pool, FishingCatchKind kind) {
        double roll = Math.random() * 100;
        double cursor = 0;
        for (FishingTableDefinition.LootEntry entry : pool) {
            cursor += entry.chance();
            if (roll <= cursor) {
                return new FishingCatchResult(kind, entry.itemId(), null, null, entry.amount(), entry.skillXp(), false, null);
            }
        }
        return pool.isEmpty() ? null : new FishingCatchResult(kind, pool.getFirst().itemId(), null, null, pool.getFirst().amount(), pool.getFirst().skillXp(), false, null);
    }

    private static FishingTableDefinition findTable(FishingContext context) {
        FishingTableDefinition fallback = null;
        for (FishingTableDefinition definition : FishingRegistry.getTables()) {
            if (!definition.mediums().isEmpty() && !definition.mediums().contains(context.medium())) {
                continue;
            }
            if (context.regionId() != null && definition.regions().contains(context.regionId())) {
                return definition;
            }
            if (definition.regions().isEmpty() && fallback == null) {
                fallback = definition;
            }
        }
        return fallback;
    }

    private static double getTotalStatistic(FishingContext context, ItemStatistic statistic) {
        double total = context.rod().getAttributeHandler().getStatistics().getOverall(statistic)
            + FishingRodPartService.getStatistics(context.rod()).getOverall(statistic);
        total += context.hotspotBuffs().getOverall(statistic);
        if (context.bait() != null) {
            SkyBlockItem baitItem = FishingItemSupport.getItem(context.bait().getItemId());
            if (baitItem != null) {
                total += baitItem.getAttributeHandler().getStatistics().getOverall(statistic);
            }
        }
        return total;
    }

    private static double getTagBonus(FishingContext context, List<String> tags) {
        double total = 0.0D;
        if (context.hook() != null) {
            total += getTagBonus(context.hook().getTagBonuses(), tags);
        }
        if (context.line() != null) {
            total += getTagBonus(context.line().getTagBonuses(), tags);
        }
        if (context.sinker() != null) {
            total += getTagBonus(context.sinker().getTagBonuses(), tags);
        }
        if (context.bait() != null) {
            total += getTagBonus(context.bait().getTagBonuses(), tags);
        }
        return total;
    }

    private static double getTagBonus(java.util.Map<String, Double> bonuses, List<String> tags) {
        double total = 0.0D;
        for (String tag : tags) {
            total += bonuses.getOrDefault(tag, 0.0D);
        }
        return total;
    }
}
