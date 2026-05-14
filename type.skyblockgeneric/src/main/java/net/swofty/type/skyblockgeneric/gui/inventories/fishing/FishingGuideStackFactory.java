package net.swofty.type.skyblockgeneric.gui.inventories.fishing;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.fishing.FishingMedium;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.components.FishingBaitComponent;
import net.swofty.type.skyblockgeneric.item.components.FishingRodPartComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FishingGuideStackFactory {
    private static final List<ItemStatistic> GUIDE_STAT_ORDER = List.of(
        ItemStatistic.DAMAGE,
        ItemStatistic.STRENGTH,
        ItemStatistic.FEROCITY,
        ItemStatistic.FISHING_SPEED,
        ItemStatistic.SEA_CREATURE_CHANCE,
        ItemStatistic.DOUBLE_HOOK_CHANCE,
        ItemStatistic.TREASURE_CHANCE,
        ItemStatistic.TROPHY_FISH_CHANCE,
        ItemStatistic.MAGIC_FIND
    );

    private FishingGuideStackFactory() {
    }

    public static net.minestom.server.item.ItemStack.Builder buildBaitStack(SkyBlockItem baitItem) {
        FishingBaitComponent bait = baitItem.getComponent(FishingBaitComponent.class);
        List<String> lore = new ArrayList<>();
        lore.add("§8Fishing Bait");
        lore.add("§8Consumes on Cast");
        lore.add("");
        appendStatistics(lore, baitItem.getAttributeHandler().getStatistics());
        appendTagBonuses(lore, bait.getTagBonuses());

        if (bait.getTreasureChanceBonus() > 0) {
            lore.add("§7Grants §6+" + format(bait.getTreasureChanceBonus()) + " Treasure Chance§7.");
        }
        if (bait.getTreasureQualityBonus() > 0) {
            lore.add("§7Increases treasure quality by §a" + format(bait.getTreasureQualityBonus()) + "%§7.");
        }
        if (bait.getTrophyFishChanceBonus() > 0) {
            lore.add("§7Grants §6+" + format(bait.getTrophyFishChanceBonus()) + " Trophy Fish Chance§7.");
        }
        if (bait.getDoubleHookChanceBonus() > 0) {
            lore.add("§7Grants §9+" + format(bait.getDoubleHookChanceBonus()) + " Double Hook Chance§7.");
        }
        if (bait.getMediums().size() == 1) {
            lore.add("§7Usable in " + (bait.getMediums().getFirst() == FishingMedium.WATER ? "§bWater" : "§cLava") + "§7.");
        }
        finishFooter(lore, bait.getItemId(), "BAIT");

        return ItemStackCreator.getStackHead(
            coloredName(bait.getItemId(), bait.getDisplayName()),
            bait.getTexture(),
            1,
            lore.toArray(String[]::new)
        );
    }

    public static net.minestom.server.item.ItemStack.Builder buildRodPartStack(SkyBlockItem partItem) {
        FishingRodPartComponent part = partItem.getComponent(FishingRodPartComponent.class);
        List<String> lore = new ArrayList<>();
        lore.add("§8" + StringUtility.toNormalCase(part.getCategory().name()) + " Rod Part");
        lore.add("");
        appendStatistics(lore, partItem.getAttributeHandler().getStatistics());
        appendTagBonuses(lore, part.getTagBonuses());

        if (part.isTreasureOnly()) {
            lore.add("§7Only allows you to catch items and §6Treasure§7.");
        }
        if (part.isBayouTreasureToJunk()) {
            lore.add("§7Replaces §6Treasure §7catches with §2Junk §7in the §2Backwater Bayou§7.");
        }
        if (part.getMaterializedItemId() != null) {
            String itemName = ItemType.valueOf(part.getMaterializedItemId()).getDisplayName();
            if (part.getMaterializedChance() >= 1.0D) {
                lore.add("§7Materializes §f" + itemName + " §7in your inventory whenever you catch something.");
            } else {
                lore.add("§7Has a §a" + format(part.getMaterializedChance() * 100.0D) + "% §7chance to materialize §f" + itemName + "§7.");
            }
        }
        if (part.getBaitPreservationChance() > 0) {
            lore.add("§7Grants a §a" + format(part.getBaitPreservationChance()) + "% §7chance to not consume Bait.");
        }
        if (part.getHotspotBuffMultiplier() > 1.0D) {
            lore.add("§7Increases the bonuses of §dFishing Hotspots §7by §a" + format((part.getHotspotBuffMultiplier() - 1.0D) * 100.0D) + "%§7.");
        }
        if (part.getRequiredFishingLevel() > 0) {
            lore.add("");
            lore.add("§4❣ §cRequires §aFishing Skill " + part.getRequiredFishingLevel() + "§c.");
        }
        finishFooter(lore, part.getItemId(), "ROD PART");

        return ItemStackCreator.getStackHead(
            coloredName(part.getItemId(), part.getDisplayName()),
            part.getTexture(),
            1,
            lore.toArray(String[]::new)
        );
    }

    private static void appendStatistics(List<String> lore, ItemStatistics statistics) {
        for (ItemStatistic statistic : GUIDE_STAT_ORDER) {
            double amount = statistics.getOverall(statistic);
            if (amount == 0) {
                continue;
            }
            lore.add("§7" + statistic.getDisplayName() + ": " + statistic.getLoreColor()
                + statistic.getPrefix() + format(amount) + statistic.getSuffix());
        }
    }

    private static void appendTagBonuses(List<String> lore, Map<String, Double> bonuses) {
        for (Map.Entry<String, Double> entry : bonuses.entrySet()) {
            lore.add("§7Increases the chance to catch §e" + describeTag(entry.getKey()) + " §7by §a" + format(entry.getValue()) + "%§7.");
        }
    }

    private static void finishFooter(List<String> lore, String itemId, String suffix) {
        if (!lore.isEmpty() && !lore.getLast().isEmpty()) {
            lore.add("");
        }
        lore.add(ItemType.valueOf(itemId).rarity.getDisplay() + " " + suffix);
    }

    private static String coloredName(String itemId, String displayName) {
        return ItemType.valueOf(itemId).rarity.getColor() + displayName;
    }

    private static String describeTag(String tag) {
        return switch (tag.toUpperCase()) {
            case "COMMON" -> "Common Sea Creatures";
            case "HOTSPOT" -> "Hotspot Sea Creatures";
            case "SPOOKY" -> "Spooky Sea Creatures";
            case "WINTER" -> "Winter Sea Creatures";
            case "SHARK" -> "Sharks";
            default -> StringUtility.toNormalCase(tag.toLowerCase().replace('_', ' '));
        };
    }

    private static String format(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.0001D) {
            return String.valueOf((long) Math.rint(value));
        }
        return StringUtility.decimalify(value, 1);
    }
}
