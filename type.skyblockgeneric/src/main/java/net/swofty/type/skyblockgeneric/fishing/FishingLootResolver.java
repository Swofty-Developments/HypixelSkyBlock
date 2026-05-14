package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.data.datapoints.DatapointToggles;
import net.swofty.type.skyblockgeneric.fishing.catches.CatchPayload;
import net.swofty.type.skyblockgeneric.fishing.catches.TrophyTier;
import net.swofty.type.skyblockgeneric.fishing.tags.FishingTag;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class FishingLootResolver {

    private static final List<Function<FishingContext, Optional<CatchPayload>>> SPECIAL_CATCHES = List.of(
        FishingLootResolver::tryResolveQuestCatch,
        FishingLootResolver::tryResolveTrophyFish,
        FishingLootResolver::tryResolveSeaCreature
    );

    private static final CatchPayload DEFAULT_CATCH =
        new CatchPayload.Item("RAW_FISH", 1, 5.0D, false);

    private FishingLootResolver() {
    }

    public static CatchPayload resolve(FishingContext context) {
        return SPECIAL_CATCHES.stream()
            .map(resolver -> resolver.apply(context))
            .flatMap(Optional::stream)
            .findFirst()
            .orElseGet(() -> resolveItem(context));
    }

    private static Optional<CatchPayload> tryResolveTrophyFish(FishingContext context) {
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
                TrophyTier tier = rollTrophyTier(context, definition);
                String itemId = switch (tier) {
                    case DIAMOND -> definition.diamondItemId();
                    case GOLD -> definition.goldItemId();
                    case SILVER -> definition.silverItemId();
                    case BRONZE -> definition.bronzeItemId();
                };
                if (itemId == null) {
                    continue;
                }
                return Optional.of(new CatchPayload.TrophyFish(definition.id(), tier, itemId, 300.0D));
            }
        }

        return Optional.empty();
    }

    private static Optional<CatchPayload> tryResolveQuestCatch(FishingContext context) {
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

        return Optional.of(new CatchPayload.Quest(
            ItemType.RUSTY_SHIP_ENGINE.name(),
            1,
            15.0D,
            "You fished up a Rusty Ship Engine!"
        ));
    }

    private static TrophyTier rollTrophyTier(FishingContext context, TrophyFishDefinition definition) {
        var progress = context.player().getTrophyFishData().getProgress(definition.id());
        if (progress.getTotalCatches() + 1 >= 600 && !progress.hasTier(TrophyTier.DIAMOND.name())) {
            return TrophyTier.DIAMOND;
        }
        if (progress.getTotalCatches() + 1 >= 100 && !progress.hasTier(TrophyTier.GOLD.name())) {
            return TrophyTier.GOLD;
        }

        double charmBonus = 0.0D;
        var charm = context.rod().getAttributeHandler().getEnchantment(net.swofty.type.skyblockgeneric.enchantment.EnchantmentType.CHARM);
        if (charm != null) {
            charmBonus = charm.level() * 2.0D;
        }

        if (Math.random() <= (0.002D * (1 + charmBonus / 100D))) return TrophyTier.DIAMOND;
        if (Math.random() <= (0.02D * (1 + charmBonus / 100D))) return TrophyTier.GOLD;
        if (Math.random() <= (0.25D * (1 + charmBonus / 100D))) return TrophyTier.SILVER;
        return TrophyTier.BRONZE;
    }

    private static Optional<CatchPayload> tryResolveSeaCreature(FishingContext context) {
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
            if (definition != null && !definition.isAvailable(context)) {
                continue;
            }
            double tagBonus = definition == null ? 0.0D : getTagBonus(context, definition.tags());
            if (Math.random() * 100 <= roll.chance() + seaCreatureChance + tagBonus) {
                double skillXp = definition == null ? 0.0D : definition.skillXp();
                CatchPayload.SeaCreature payload = new CatchPayload.SeaCreature(roll.seaCreatureId(), skillXp);
                if (rollDoubleHook(context)) {
                    payload = payload.withDoubleHook();
                }
                return Optional.of(payload);
            }
        }
        return Optional.empty();
    }

    private static CatchPayload resolveItem(FishingContext context) {
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
            return pick(pool, true).orElse(DEFAULT_CATCH);
        }

        return pick(pool, false)
            .or(() -> table.junk().isEmpty() ? Optional.empty() : pick(table.junk(), false))
            .orElse(DEFAULT_CATCH);
    }

    private static Optional<CatchPayload> pick(List<FishingTableDefinition.LootEntry> pool, boolean fromTreasure) {
        if (pool.isEmpty()) return Optional.empty();

        double roll = Math.random() * 100;
        double cursor = 0;
        for (FishingTableDefinition.LootEntry entry : pool) {
            cursor += entry.chance();
            if (roll <= cursor) {
                return Optional.of(new CatchPayload.Item(entry.itemId(), entry.amount(), entry.skillXp(), fromTreasure));
            }
        }
        FishingTableDefinition.LootEntry first = pool.getFirst();
        return Optional.of(new CatchPayload.Item(first.itemId(), first.amount(), first.skillXp(), fromTreasure));
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

    private static boolean rollDoubleHook(FishingContext context) {
        double chance = getTotalStatistic(context, ItemStatistic.DOUBLE_HOOK_CHANCE);
        if (context.bait() != null) {
            chance += context.bait().getDoubleHookChanceBonus();
        }
        return chance > 0 && Math.random() * 100 <= chance;
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

    private static double getTagBonus(FishingContext context, List<FishingTag> tags) {
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

    private static double getTagBonus(java.util.Map<String, Double> bonuses, List<FishingTag> tags) {
        double total = 0.0D;
        for (FishingTag tag : tags) {
            total += bonuses.getOrDefault(tag.id(), 0.0D);
        }
        return total;
    }
}
