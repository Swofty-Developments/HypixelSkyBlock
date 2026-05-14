package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class FishingLootResolver {

    private static final List<Function<FishingContext, Optional<FishingCatchResult>>> SPECIAL_CATCHES = List.of(
        FishingLootResolver::tryResolveQuestCatch,
        FishingLootResolver::tryResolveTrophyFish,
        FishingLootResolver::tryResolveSeaCreature
    );

    private static final FishingCatchResult DEFAULT_CATCH =
        new FishingCatchResult(FishingCatchKind.ITEM, "RAW_FISH", null, null, 1, 5.0D, false, null);

    private FishingLootResolver() {
    }

    public static FishingCatchResult resolve(FishingContext context) {
        return SPECIAL_CATCHES.stream()
            .map(resolver -> resolver.apply(context))
            .flatMap(Optional::stream)
            .findFirst()
            .orElseGet(() -> resolveItem(context));
    }

    private static Optional<FishingCatchResult> tryResolveTrophyFish(FishingContext context) {
        if (context.medium() != FishingMedium.LAVA) {
            return Optional.empty();
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
                return Optional.of(new FishingCatchResult(
                    FishingCatchKind.TROPHY_FISH, itemId, null, definition.id(), 1, 300.0D, false, null));
            }
        }

        return Optional.empty();
    }

    private static Optional<FishingCatchResult> tryResolveQuestCatch(FishingContext context) {
        if (context.medium() != FishingMedium.WATER) return Optional.empty();
        if (context.regionId() == null) return Optional.empty();
        if (!"FISHING_OUTPOST".equals(context.regionId()) && !"FISHERMANS_HUT".equals(context.regionId())) {
            return Optional.empty();
        }
        if (!context.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_SPOKEN_TO_FISHERWOMAN_ENID)) {
            return Optional.empty();
        }
        if (context.player().getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_UNLOCKED_SHIP)) {
            return Optional.empty();
        }
        if (context.player().getShipState().getEngine() != null) return Optional.empty();
        if (context.player().countItem(ItemType.RUSTY_SHIP_ENGINE) > 0) return Optional.empty();
        if (Math.random() * 100 > 1.0D) return Optional.empty();

        return Optional.of(new FishingCatchResult(
            FishingCatchKind.QUEST,
            ItemType.RUSTY_SHIP_ENGINE.name(),
            null,
            null,
            1,
            15.0D,
            false,
            "You fished up a Rusty Ship Engine!"
        ));
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

        if (Math.random() <= (0.002D * (1 + charmBonus / 100D))) return "DIAMOND";
        if (Math.random() <= (0.02D * (1 + charmBonus / 100D))) return "GOLD";
        if (Math.random() <= (0.25D * (1 + charmBonus / 100D))) return "SILVER";
        return "BRONZE";
    }

    private static Optional<FishingCatchResult> tryResolveSeaCreature(FishingContext context) {
        Optional<FishingTableDefinition> tableOpt = findTable(context);
        if (tableOpt.isEmpty()) return Optional.empty();
        FishingTableDefinition table = tableOpt.get();

        double seaCreatureChance = getTotalStatistic(context, ItemStatistic.SEA_CREATURE_CHANCE);
        if (context.hook() != null && context.hook().isTreasureOnly()) {
            return Optional.empty();
        }

        for (FishingTableDefinition.SeaCreatureRoll roll : table.seaCreatures()) {
            SeaCreatureDefinition definition = FishingRegistry.getSeaCreature(roll.seaCreatureId());
            if (definition != null && context.player().getSkills().getCurrentLevel(net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING) < definition.requiredFishingLevel()) {
                continue;
            }
            double tagBonus = definition == null ? 0.0D : getTagBonus(context, definition.tags());
            if (Math.random() * 100 <= roll.chance() + seaCreatureChance + tagBonus) {
                double skillXp = definition == null ? 0.0D : definition.skillXp();
                return Optional.of(new FishingCatchResult(
                    FishingCatchKind.SEA_CREATURE, null, roll.seaCreatureId(), null, 1, skillXp, false, null));
            }
        }
        return Optional.empty();
    }

    private static FishingCatchResult resolveItem(FishingContext context) {
        Optional<FishingTableDefinition> tableOpt = findTable(context);
        if (tableOpt.isEmpty()) return DEFAULT_CATCH;
        FishingTableDefinition table = tableOpt.get();

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
            return pick(pool, FishingCatchKind.TREASURE).orElse(DEFAULT_CATCH);
        }

        return pick(pool, FishingCatchKind.ITEM)
            .or(() -> table.junk().isEmpty() ? Optional.empty() : pick(table.junk(), FishingCatchKind.ITEM))
            .orElse(DEFAULT_CATCH);
    }

    private static Optional<FishingCatchResult> pick(List<FishingTableDefinition.LootEntry> pool, FishingCatchKind kind) {
        if (pool.isEmpty()) return Optional.empty();

        double roll = Math.random() * 100;
        double cursor = 0;
        for (FishingTableDefinition.LootEntry entry : pool) {
            cursor += entry.chance();
            if (roll <= cursor) {
                return Optional.of(new FishingCatchResult(
                    kind, entry.itemId(), null, null, entry.amount(), entry.skillXp(), false, null));
            }
        }
        FishingTableDefinition.LootEntry first = pool.getFirst();
        return Optional.of(new FishingCatchResult(
            kind, first.itemId(), null, null, first.amount(), first.skillXp(), false, null));
    }

    private static Optional<FishingTableDefinition> findTable(FishingContext context) {
        FishingTableDefinition fallback = null;
        for (FishingTableDefinition definition : FishingRegistry.getTables()) {
            if (!definition.mediums().isEmpty() && !definition.mediums().contains(context.medium())) {
                continue;
            }
            if (context.regionId() != null && definition.regions().contains(context.regionId())) {
                return Optional.of(definition);
            }
            if (definition.regions().isEmpty() && fallback == null) {
                fallback = definition;
            }
        }
        return Optional.ofNullable(fallback);
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
