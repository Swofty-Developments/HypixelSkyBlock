package net.swofty.type.skyblockgeneric.fishing;

import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import net.swofty.commons.skyblock.item.reforge.Reforge;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class FishingRodLoreBuilder {
    private static final List<ItemStatistic> STAT_ORDER = List.of(
        ItemStatistic.DAMAGE,
        ItemStatistic.STRENGTH,
        ItemStatistic.FEROCITY,
        ItemStatistic.FISHING_SPEED,
        ItemStatistic.SEA_CREATURE_CHANCE,
        ItemStatistic.TREASURE_CHANCE,
        ItemStatistic.TROPHY_FISH_CHANCE,
        ItemStatistic.MAGIC_FIND
    );

    private FishingRodLoreBuilder() {
    }

    public static @Nullable FishingRodLore build(SkyBlockItem item, @Nullable SkyBlockPlayer player) {
        ItemType itemType = item.getAttributeHandler().getPotentialType();
        if (itemType == null) {
            return null;
        }

        FishingRodDefinition definition = FishingItemCatalog.getRod(itemType.name());
        if (definition == null) {
            return null;
        }

        var handler = item.getAttributeHandler();
        Rarity rarity = handler.isRecombobulated() ? handler.getRarity().upgrade() : handler.getRarity();

        String displayName = definition.displayName();
        Reforge reforge = handler.getReforge();
        if (reforge != null) {
            displayName = reforge.getPrefix() + " " + displayName;
        }
        displayName = rarity.getColor() + displayName;

        List<String> lore = new ArrayList<>();
        if (definition.subtitle() != null) {
            lore.add("§8" + definition.subtitle());
            lore.add("");
        }

        for (ItemStatistic statistic : STAT_ORDER) {
            String line = buildStatLine(item, definition, rarity, statistic);
            if (line != null) {
                lore.add(line);
            }
        }

        lore.addAll(definition.lore());
        if (!lore.isEmpty() && !lore.getLast().isEmpty()) {
            lore.add("");
        }

        List<String> enchantLines = buildEnchantLines(item, rarity);
        lore.addAll(enchantLines);
        if (!enchantLines.isEmpty()) {
            lore.add("");
        }

        if (definition.rodPartsEnabled()) {
            RodPartDefinition hook = FishingRodPartService.getHook(item);
            RodPartDefinition line = FishingRodPartService.getLine(item);
            RodPartDefinition sinker = FishingRodPartService.getSinker(item);

            lore.add(renderPartHeader("ථ", "Hook", hook));
            if (hook != null) {
                lore.addAll(renderAppliedPartLore(hook, player));
            }
            lore.add(renderPartHeader("ꨃ", "Line", line));
            if (line != null) {
                lore.addAll(renderAppliedPartLore(line, player));
            }
            lore.add(renderPartHeader("࿉", "Sinker", sinker));
            if (sinker != null) {
                lore.addAll(renderAppliedPartLore(sinker, player));
            }
            lore.add("");
            lore.add("§7Talk to §2Roddy §7in the §2Backwater");
            lore.add("§2Bayou §7to apply parts to this rod.");
            lore.add("");
        } else if (definition.legacyConversionTarget() != null) {
            lore.add("§7§cThis rod is broken and cannot be");
            lore.add("§cused to fish.");
            lore.add("");
            lore.add("§7Bring it to §2Roddy §7in the §2Backwater");
            lore.add("§2Bayou §7to convert it into a §anew rod§7!");
            lore.add("");
        }

        lore.add("§8This item can be reforged!");

        if (handler.getFishingExpertiseKills() > 0) {
            lore.add("§fKills: §6" + StringUtility.commaify(handler.getFishingExpertiseKills()));
        }

        if (handler.getSoulBoundData() != null) {
            lore.add("§8§l* Co-op Soulbound §l*");
        }

        if (definition.requiredFishingLevel() > 0 && player != null &&
            player.getSkills().getCurrentLevel(net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING) < definition.requiredFishingLevel()) {
            lore.add("§4❣ §cRequires §aFishing Skill " + definition.requiredFishingLevel() + "§c.");
        }

        String footer = rarity.getDisplay() + " FISHING ROD";
        if (handler.isRecombobulated()) {
            footer = rarity.getColor() + "§l§ka §l" + rarity.name() + " FISHING ROD §l§ka";
        }
        lore.add(footer);

        return new FishingRodLore(displayName, lore);
    }

    private static @Nullable String buildStatLine(SkyBlockItem item, FishingRodDefinition definition, Rarity rarity, ItemStatistic statistic) {
        double base = definition.statistics().getOverall(statistic);
        double reforge = 0.0D;
        if (item.getAttributeHandler().getReforge() != null) {
            reforge = item.getAttributeHandler().getReforge().getAfterCalculation(ItemStatistics.empty(), rarity.ordinal() + 1).getOverall(statistic);
        }

        double part = FishingRodPartService.getStatistics(item).getOverall(statistic);
        double dynamic = item.getAttributeHandler().getExtraDynamicStatistics().getOverall(statistic);
        double hpb = getHotPotatoContribution(item, statistic);
        double total = base + reforge + part + dynamic + hpb;

        if (total == 0) {
            return null;
        }

        String line = "§7" + statistic.getDisplayName() + ": " + statistic.getLoreColor()
            + statistic.getPrefix() + formatNumber(total) + statistic.getSuffix();
        if (hpb != 0) {
            line += " §e(" + (hpb > 0 ? "+" : "") + formatNumber(hpb) + ")";
        }
        if (part != 0) {
            line += " §d(" + (part > 0 ? "+" : "") + formatNumber(part) + ")";
        } else if (reforge != 0) {
            line += " §9(" + (reforge > 0 ? "+" : "") + formatNumber(reforge) + ")";
        }
        return line;
    }

    private static double getHotPotatoContribution(SkyBlockItem item, ItemStatistic statistic) {
        ItemAttributeHotPotatoBookData.HotPotatoBookData data = item.getAttributeHandler().getHotPotatoBookData();
        if (!data.hasAppliedItem()) {
            return 0.0D;
        }

        double total = 0.0D;
        for (Map.Entry<ItemStatistic, Double> entry : data.getPotatoType().stats.entrySet()) {
            if (entry.getKey() == statistic) {
                total += entry.getValue();
            }
        }
        return total;
    }

    private static List<String> buildEnchantLines(SkyBlockItem item, Rarity rarity) {
        List<SkyBlockEnchantment> enchantments = item.getAttributeHandler().getEnchantments()
            .sorted(Comparator.comparing(enchantment -> enchantment.type().getName()))
            .toList();
        if (enchantments.isEmpty()) {
            return List.of();
        }

        String enchantColor = rarity == Rarity.MYTHIC ? "§d" : "§9";
        String joined = enchantments.stream()
            .map(enchantment -> enchantColor + enchantment.type().getName() + " " + StringUtility.getAsRomanNumeral(enchantment.level()))
            .reduce((left, right) -> left + ", " + right)
            .orElse("");
        return StringUtility.splitByWordAndLength(joined, 34);
    }

    private static String renderPartHeader(String symbol, String label, @Nullable RodPartDefinition part) {
        if (part == null) {
            return "§9" + symbol + " " + label + " §8§lNONE";
        }
        return ItemType.valueOf(part.itemId()).rarity.getColor() + symbol + " " + part.displayName();
    }

    private static List<String> renderAppliedPartLore(RodPartDefinition part, @Nullable SkyBlockPlayer player) {
        List<String> lines = new ArrayList<>();
        for (String line : part.lore()) {
            lines.add("§7" + line);
        }
        if (part.requiredFishingLevel() > 0 && player != null &&
            player.getSkills().getCurrentLevel(net.swofty.type.skyblockgeneric.skill.SkillCategories.FISHING) < part.requiredFishingLevel()) {
            lines.add("§4❣ §cRequires §aFishing Skill " + part.requiredFishingLevel() + "§c.");
        }
        return lines;
    }

    private static String formatNumber(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.0001D) {
            return String.valueOf((long) Math.rint(value));
        }
        return StringUtility.decimalify(value, 1);
    }

    public record FishingRodLore(String displayName, List<String> lore) {
    }
}
