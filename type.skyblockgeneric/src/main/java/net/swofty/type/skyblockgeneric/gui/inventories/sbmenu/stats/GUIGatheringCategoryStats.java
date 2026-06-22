package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import lombok.Getter;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.gui.v2.Components;
import net.swofty.type.generic.gui.v2.DefaultState;
import net.swofty.type.generic.gui.v2.Layouts;
import net.swofty.type.generic.gui.v2.StatelessView;
import net.swofty.type.generic.gui.v2.ViewConfiguration;
import net.swofty.type.generic.gui.v2.ViewLayout;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;
import net.swofty.type.skyblockgeneric.user.statistics.StatisticModifier;
import net.swofty.type.skyblockgeneric.user.statistics.StatisticModifierType;
import net.swofty.type.skyblockgeneric.user.statistics.StatisticSourceType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GUIGatheringCategoryStats extends StatelessView {
    private static final int[] DISPLAY_SLOTS = {
        10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29,
        30, 31, 32, 33, 34
    };

    private final Category category;
    private final ItemStatistic statistic;
    private final Mode mode;
    private final boolean showAll;
    private final boolean groupByCategory;
    private final boolean flattened;
    private final StatisticSourceType sourceFilter;
    private final StatisticModifierType modifierFilter;

    public GUIGatheringCategoryStats(Category category) {
        this(category, null, Mode.CATEGORY, true, false, false, null, null);
    }

    private GUIGatheringCategoryStats(Category category, ItemStatistic statistic, Mode mode,
                                      boolean showAll, boolean groupByCategory, boolean flattened,
                                      StatisticSourceType sourceFilter, StatisticModifierType modifierFilter) {
        this.category = category;
        this.statistic = statistic;
        this.mode = mode;
        this.showAll = showAll;
        this.groupByCategory = groupByCategory;
        this.flattened = flattened;
        this.sourceFilter = sourceFilter;
        this.modifierFilter = modifierFilter;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        String title = switch (mode) {
            case CATEGORY -> "Your Stats Breakdown";
            case DETAIL -> "Stats ➜ " + statistic.getDisplayName();
            case FLAT -> statistic.getDisplayName() + " ➜ Flat Bonuses";
            case ADDITIVE -> statistic.getDisplayName() + " ➜ Additive Buffs";
        };
        return new ViewConfiguration<>(title, InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.filler(Layouts.border(0, 53), Components.FILLER);
        Components.back(layout, 48, ctx);
        Components.close(layout, 49);

        // dumb way but it works for now
        switch (mode) {
            case CATEGORY -> categoryLayout(layout);
            case DETAIL -> detailLayout(layout, ctx);
            case FLAT, ADDITIVE -> breakdownLayout(layout);
        }
    }

    private void categoryLayout(ViewLayout<DefaultState> layout) {
        layout.slot(4, (s, c) -> category.createSummary((SkyBlockPlayer) c.player(), false));

        // TODO: using openView breaks back button
        layout.slot(50, toggleItem(), (_, c) -> c.player().openView(
            new GUIGatheringCategoryStats(category, null, Mode.CATEGORY, !showAll, false, false, null, null)));

        for (int index = 0; index < Math.min(category.statistics.size(), DISPLAY_SLOTS.length); index++) {
            int statisticIndex = index;
            layout.slot(DISPLAY_SLOTS[index], (s, c) -> {
                ItemStatistic stat = visibleStatistics((SkyBlockPlayer) c.player()).get(statisticIndex);
                return stat == null ? net.minestom.server.item.ItemStack.AIR.builder()
                    : createStatisticItem((SkyBlockPlayer) c.player(), stat, true);
            }, (_, c) -> {
                ItemStatistic stat = visibleStatistics((SkyBlockPlayer) c.player()).get(statisticIndex);
                if (stat != null) openStatistic((SkyBlockPlayer) c.player(), stat);
            });
        }
    }

    private void openStatistic(SkyBlockPlayer player, ItemStatistic stat) {
        ItemStatistics values = player.getStatistics().allStatistics();
        Mode target = values.getAdditive(stat) == 1D && values.getMultiplicative(stat) == 1D
            ? Mode.FLAT : Mode.DETAIL;
        player.openView(new GUIGatheringCategoryStats(category, stat, target, showAll, false, false, null, null));
    }

    private void detailLayout(ViewLayout<DefaultState> layout, ViewContext ctx) {
        layout.slot(4, (s, c) -> createStatisticItem((SkyBlockPlayer) c.player(), statistic, false));
        {
            SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
            ItemStatistics values = player.getStatistics().allStatistics();
            boolean additive = values.getAdditive(statistic) != 1D;
            boolean capped = statistic.getCap() != null;
            int flatSlot = capped ? 20 : 21;
            int additiveSlot = capped ? 22 : 23;

            layout.slot(flatSlot, (s, c) -> summaryItem((SkyBlockPlayer) c.player(), Mode.FLAT),
                (_, c) -> c.player().openView(copy(Mode.FLAT)));
            if (additive) {
                layout.slot(additiveSlot - 1, StatisticArrow.create(statistic),
                    (_, c) -> c.player().openView(copy(Mode.ADDITIVE)));
                layout.slot(additiveSlot, (s, c) -> summaryItem((SkyBlockPlayer) c.player(), Mode.ADDITIVE),
                    (_, c) -> c.player().openView(copy(Mode.ADDITIVE)));
            }
            if (capped) {
                layout.slot(23, StatisticArrow.create(statistic));
                layout.slot(24, capItem());
            }
        }
    }

    private void breakdownLayout(ViewLayout<DefaultState> layout) {
        for (int index = 0; index < DISPLAY_SLOTS.length; index++) {
            int sourceIndex = index;
            layout.slot(DISPLAY_SLOTS[index], (s, c) -> {
                List<ViewEntry> entries = visibleEntries((SkyBlockPlayer) c.player());
                if (sourceIndex >= entries.size()) return net.minestom.server.item.ItemStack.AIR.builder();
                return sourceItem(entries.get(sourceIndex));
            }, (_, c) -> {
                List<ViewEntry> entries = visibleEntries((SkyBlockPlayer) c.player());
                if (sourceIndex >= entries.size()) return;
                ViewEntry entry = entries.get(sourceIndex);
                if (!entry.grouped()) return;
                c.player().openView(new GUIGatheringCategoryStats(category, statistic, mode, showAll,
                    groupByCategory, flattened, entry.sourceFilter(), entry.modifierFilter()));
            });
        }
        layout.slot(50, optionItem("Group By Category", Material.NAME_TAG, groupByCategory),
            (_, c) -> c.player().openView(rootCopy(mode, !groupByCategory, flattened)));
        layout.slot(51, optionItem("Flatten Stats Menu",
                flattened ? Material.COBBLESTONE_SLAB : Material.COBBLESTONE, flattened),
            (_, c) -> c.player().openView(rootCopy(mode, groupByCategory, !flattened)));
    }

    private GUIGatheringCategoryStats copy(Mode next) {
        return copy(next, groupByCategory, flattened);
    }

    private GUIGatheringCategoryStats copy(Mode next, boolean grouped, boolean flat) {
        return new GUIGatheringCategoryStats(category, statistic, next, showAll, grouped, flat,
            sourceFilter, modifierFilter);
    }

    private GUIGatheringCategoryStats rootCopy(Mode next, boolean grouped, boolean flat) {
        return new GUIGatheringCategoryStats(category, statistic, next, showAll, grouped, flat, null, null);
    }

    private List<ItemStatistic> visibleStatistics(SkyBlockPlayer player) {
        List<ItemStatistic> visible = category.statistics.stream()
            .filter(stat -> showAll || overall(player, stat) != 0D).toList();
        return new java.util.AbstractList<>() {
            @Override
            public ItemStatistic get(int index) {
                return index < visible.size() ? visible.get(index) : null;
            }

            @Override
            public int size() {
                return Math.max(visible.size(), category.statistics.size());
            }
        };
    }

    private List<ViewEntry> visibleEntries(SkyBlockPlayer player) {
        if (flattened) return flattenedEntries(player);
        List<PlayerStatistics.StatisticSource> sources = player.getStatistics().statisticSources().stream()
            .filter(source -> sourceValue(source.statistics()) != 0D)
            .filter(source -> sourceFilter == null || source.sourceType() == sourceFilter)
            .toList();
        if (!groupByCategory || sourceFilter != null) {
            return sources.stream().map(ViewEntry::fromSource)
                .sorted(Comparator.comparingDouble(this::entryValue).reversed()).toList();
        }

        Map<StatisticSourceType, List<PlayerStatistics.StatisticSource>> grouped = new LinkedHashMap<>();
        for (PlayerStatistics.StatisticSource source : sources) {
            grouped.computeIfAbsent(source.sourceType(), ignored -> new ArrayList<>()).add(source);
        }
        return grouped.entrySet().stream().map(entry -> ViewEntry.fromSources(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparingDouble(this::entryValue).reversed()).toList();
    }

    private List<ViewEntry> flattenedEntries(SkyBlockPlayer player) {
        List<StatisticModifier> modifiers = player.getStatistics().statisticModifiers().stream()
            .filter(modifier -> sourceValue(modifier.statistics()) != 0D)
            .filter(modifier -> modifierFilter == null || modifier.modifierType() == modifierFilter)
            .toList();
        if (!groupByCategory || modifierFilter != null) {
            return modifiers.stream().map(ViewEntry::fromModifier)
                .sorted(Comparator.comparingDouble(this::entryValue).reversed()).toList();
        }

        Map<StatisticModifierType, List<StatisticModifier>> grouped = new LinkedHashMap<>();
        for (StatisticModifier modifier : modifiers) {
            grouped.computeIfAbsent(modifier.modifierType(), ignored -> new ArrayList<>()).add(modifier);
        }
        return grouped.entrySet().stream().map(entry -> ViewEntry.fromModifiers(entry.getKey(), entry.getValue()))
            .sorted(Comparator.comparingDouble(this::entryValue).reversed()).toList();
    }

    private double sourceValue(ItemStatistics statistics) {
        return mode == Mode.FLAT ? statistics.getBase(statistic)
            : (statistics.getAdditive(statistic) - 1D) * 100D;
    }

    private double entryValue(ViewEntry entry) {
        return sourceValue(entry.statistics());
    }

    private net.minestom.server.item.ItemStack.Builder summaryItem(SkyBlockPlayer player, Mode summaryMode) {
        ItemStatistics values = player.getStatistics().allStatistics();
        double value = summaryMode == Mode.FLAT ? values.getBase(statistic)
            : (values.getAdditive(statistic) - 1D) * 100D;
        List<String> lore = new ArrayList<>();
        lore.add("§8" + statistic.getDisplayName() + " Stat");
        lore.add(summaryMode == Mode.FLAT ? "§7All flat bonuses are summed into" : "§7These buffs are added and converted");
        lore.add(summaryMode == Mode.FLAT ? "§7a base amount." : "§7and then converted into the");
        if (summaryMode == Mode.ADDITIVE) lore.add("§7(additive) multiplier.");
        lore.add("");
        addSourcePreview(player, lore, summaryMode);
        lore.add("");
        lore.add("§7Adds up to: " + statistic.getDisplayColor() + "+" + format(value)
            + (summaryMode == Mode.FLAT ? statistic.getSymbol() + " " + statistic.getDisplayName() : "%"));
        if (summaryMode == Mode.ADDITIVE) {
            lore.add("§7As multiplier: " + statistic.getDisplayColor() + format(1D + value / 100D) + "x");
            lore.add("§8Multiplied with flat!");
        }
        lore.add("");
        lore.add("§eClick to dig deeper!");
        return ItemStackCreator.getStack(statistic.getFullDisplayName() +
                (summaryMode == Mode.FLAT ? " Flat Bonuses" : " Additive Buffs"),
            summaryMode == Mode.FLAT ? Material.PAPER : Material.BOOK, 1, lore);
    }

    private void addSourcePreview(SkyBlockPlayer player, List<String> lore, Mode summaryMode) {
        int shown = 0;
        for (PlayerStatistics.StatisticSource source : player.getStatistics().statisticSources()) {
            double value = summaryMode == Mode.FLAT ? source.statistics().getBase(statistic)
                : (source.statistics().getAdditive(statistic) - 1D) * 100D;
            if (value == 0D) continue;
            if (shown++ == 7) {
                lore.add("  §8And more...");
                break;
            }
            lore.add(" " + statistic.getDisplayColor() + "+" + format(value)
                + (summaryMode == Mode.FLAT ? statistic.getSymbol() : "%") + " §f" + source.name());
        }
    }

    private net.minestom.server.item.ItemStack.Builder sourceItem(ViewEntry source) {
        double value = entryValue(source);
        List<String> lore = new ArrayList<>();
        lore.add(source.grouped() ? "§8Grouped" : "§8" + source.categoryName());
        lore.add("");
        lore.add("§7Value: " + statistic.getDisplayColor() + "+" + format(value)
            + (mode == Mode.FLAT ? statistic.getSymbol() : "%"));
        lore.add("");
        int shown = 0;
        if (source.children().size() > 1) {
            for (ViewEntry child : source.children()) {
                double childValue = entryValue(child);
                if (childValue == 0D) continue;
                if (shown++ == 7) {
                    lore.add("  §8And more...");
                    break;
                }
                lore.add(" " + statistic.getDisplayColor() + "+" + format(childValue)
                    + (mode == Mode.FLAT ? statistic.getSymbol() : "%") + " §f" + child.name());
            }
            lore.add("");
        }
        if (source.parentName() != null) {
            lore.add("§7Modifier flattened from:");
            lore.add("§9" + source.parentName());
            lore.add("");
        }
        lore.addAll(source.description());
        if (source.grouped()) {
            lore.add("");
            lore.add("§eClick to dig even deeper!");
        }
        String name = statistic.getFullDisplayName() + " " +
            (source.grouped() ? "Category: " : "") + source.name();
        GUIMaterial icon = source.texture() == null
            ? new GUIMaterial(source.material()) : new GUIMaterial(source.texture());
        return ItemStackCreator.getUsingGUIMaterial(name, icon, 1, lore);
    }

    private net.minestom.server.item.ItemStack.Builder capItem() {
        return ItemStackCreator.getStack(statistic.getFullDisplayName() + " Cap",
            Material.LEATHER_HELMET, 1, "§8" + statistic.getDisplayName() + " Stat",
            "§7There is a " + statistic.getDisplayName().toLowerCase() + " limit in SkyBlock!",
            "§7Some magic may let you change it!", "",
            "§7Value: " + statistic.getDisplayColor() + format(statistic.getCap())
                + statistic.getSymbol());
    }

    private record ViewEntry(String name, Material material, String texture, ItemStatistics statistics,
                             String categoryName, List<String> description, String parentName,
                             List<ViewEntry> children, boolean grouped,
                             StatisticSourceType sourceFilter, StatisticModifierType modifierFilter) {
        private static ViewEntry fromSource(PlayerStatistics.StatisticSource source) {
            List<ViewEntry> children = source.modifiers().stream().map(ViewEntry::fromModifier).toList();
            return new ViewEntry(source.name(), source.material(), source.texture(), source.statistics(),
                source.sourceType().getDisplayName(), source.sourceType().getDescription(), null,
                children, false, null, null);
        }

        private static ViewEntry fromModifier(StatisticModifier modifier) {
            return new ViewEntry(modifier.name(), modifier.material(), modifier.texture(), modifier.statistics(),
                modifier.modifierType().getDisplayName(), modifier.modifierType().getDescription(),
                modifier.parentName(), List.of(), false, null, null);
        }

        private static ViewEntry fromSources(StatisticSourceType type, List<PlayerStatistics.StatisticSource> sources) {
            ItemStatistics total = ItemStatistics.empty();
            List<ViewEntry> children = new ArrayList<>();
            for (PlayerStatistics.StatisticSource source : sources) {
                total = ItemStatistics.add(total, source.statistics());
                children.add(fromSource(source));
            }
            return new ViewEntry(type.getDisplayName(), type.getMaterial(), null, total,
                type.getDisplayName(), type.getDescription(), null, children, true, type, null);
        }

        private static ViewEntry fromModifiers(StatisticModifierType type, List<StatisticModifier> modifiers) {
            ItemStatistics total = ItemStatistics.empty();
            List<ViewEntry> children = new ArrayList<>();
            for (StatisticModifier modifier : modifiers) {
                total = ItemStatistics.add(total, modifier.statistics());
                children.add(fromModifier(modifier));
            }
            return new ViewEntry(type.getDisplayName(), type.getMaterial(), null, total,
                type.getDisplayName(), type.getDescription(), null, children, true, null, type);
        }
    }

    private net.minestom.server.item.ItemStack.Builder toggleItem() {
        return ItemStackCreator.getStack("§aToggle Show All Stats", Material.PUFFERFISH, 1,
            "§7Toggle whether you want to see", "§aALL §7SkyBlock statistics, or just",
            "§7the ones you have.", "", "§7Show all stats: " + (showAll ? "§aYes" : "§cNo"),
            "", "§eClick to toggle!");
    }

    private static net.minestom.server.item.ItemStack.Builder optionItem(String name, Material material, boolean enabled) {
        return ItemStackCreator.getStack("§a" + name, material, 1,
            name.startsWith("Group") ? "§7Groups modifiers from the same category." :
                "§7Breaks down modifiers for comparison.",
            "", "§7Enabled: " + (enabled ? "§aON" : "§cOFF"), "",
            "§eClick to " + (enabled ? "disable!" : "enable!"));
    }

    private static net.minestom.server.item.ItemStack.Builder createStatisticItem(
        SkyBlockPlayer player, ItemStatistic stat, boolean clickable) {
        ItemStatistics values = player.getStatistics().allStatistics();
        double value = values.getOverall(stat);
        List<String> lore = new ArrayList<>(ItemStatistics.getDescription(stat));
        lore.add("");
        double base = values.getBase(stat);
        double additive = values.getAdditive(stat) - 1D;
        if (base != 0D) lore.add("§7Flat: " + stat.getDisplayColor() + "+" + format(base) + stat.getSymbol());
        if (additive != 0D) lore.add("§7Additive: " + stat.getDisplayColor() + "+" + format(additive * 100D) + "%");
        if (stat.getCap() != null) lore.add("§7Stat Cap: " + stat.getDisplayColor()
            + format(stat.getCap()) + stat.getSymbol() + " " + stat.getDisplayName());
        if (base != 0D || additive != 0D) lore.add("");
        if (stat.name().endsWith("_FORTUNE")) {
            lore.add("§7Bonus drops: " + stat.getDisplayColor() + "+" + (int) (value / 100D) + "!");
            lore.add("§7Chance for 1 more: " + stat.getDisplayColor() + format(value % 100D) + "%");
            lore.add("");
        }
        if (value == 0D) lore.add("§8You have none of this stat!");
        if (clickable) lore.add("§eClick to view!");
        GUIMaterial material = stat.getIconTexture() == null
            ? new GUIMaterial(stat.getIconMaterial()) : new GUIMaterial(stat.getIconTexture());
        return ItemStackCreator.getUsingGUIMaterial(stat.getFullDisplayName() + " §f" + format(value)
            + stat.getSuffix(), material, 1, lore);
    }

    private static double overall(SkyBlockPlayer player, ItemStatistic statistic) {
        return player.getStatistics().allStatistics().getOverall(statistic);
    }

    private static String format(double value) {
        return StringUtility.decimalify(value, 2);
    }

    @Override
    public boolean onBottomClick(net.swofty.type.generic.gui.v2.context.ClickContext<DefaultState> click, ViewContext ctx) {
        return true;
    }

    private enum Mode {CATEGORY, DETAIL, FLAT, ADDITIVE}

    @Getter
    public enum Category {
        COMBAT("§cCombat Stats", Material.STONE_SWORD, List.of("§7Stats that influence damage dealt", "§7and damage taken in combat."), List.of(
            ItemStatistic.HEALTH, ItemStatistic.DEFENSE, ItemStatistic.TRUE_DEFENSE, ItemStatistic.STRENGTH,
            ItemStatistic.CRITICAL_CHANCE, ItemStatistic.CRITICAL_DAMAGE, ItemStatistic.BONUS_ATTACK_SPEED,
            ItemStatistic.FEROCITY, ItemStatistic.SWING_RANGE, ItemStatistic.INTELLIGENCE,
            ItemStatistic.ABILITY_DAMAGE, ItemStatistic.HEALTH_REGENERATION, ItemStatistic.VITALITY, ItemStatistic.MENDING)),
        MINING("§6Mining Stats", Material.STONE_PICKAXE, List.of("§7Stats that influence mining speed,", "§7power, spread, and drops."), List.of(
            ItemStatistic.BREAKING_POWER, ItemStatistic.MINING_SPEED, ItemStatistic.MINING_SPREAD,
            ItemStatistic.GEMSTONE_SPREAD, ItemStatistic.PRISTINE, ItemStatistic.MINING_FORTUNE,
            ItemStatistic.ORE_FORTUNE, ItemStatistic.BLOCK_FORTUNE, ItemStatistic.DWARVEN_METAL_FORTUNE,
            ItemStatistic.GEMSTONE_FORTUNE)),
        FARMING("§aFarming Stats", Material.GOLDEN_HOE, List.of("§7Stats that influence crop drops", "§7and pest spawns."), List.of(
            ItemStatistic.BONUS_PEST_CHANCE, ItemStatistic.OVERBLOOM, ItemStatistic.FARMING_FORTUNE,
            ItemStatistic.WHEAT_FORTUNE, ItemStatistic.CARROT_FORTUNE, ItemStatistic.POTATO_FORTUNE,
            ItemStatistic.PUMPKIN_FORTUNE, ItemStatistic.SUGAR_CANE_FORTUNE, ItemStatistic.MELON_FORTUNE,
            ItemStatistic.CACTUS_FORTUNE, ItemStatistic.COCOA_BEANS_FORTUNE, ItemStatistic.MUSHROOM_FORTUNE,
            ItemStatistic.NETHER_WART_FORTUNE, ItemStatistic.SUNFLOWER_FORTUNE,
            ItemStatistic.MOONFLOWER_FORTUNE, ItemStatistic.WILD_ROSE_FORTUNE)),
        FORAGING("§2Foraging Stats", Material.JUNGLE_SAPLING, List.of("§7Stats that influence drops", "§7received while foraging."), List.of(
            ItemStatistic.SWEEP, ItemStatistic.FORAGING_FORTUNE, ItemStatistic.FIG_FORTUNE, ItemStatistic.MANGROVE_FORTUNE)),
        FISHING("§bFishing Stats", Material.FISHING_ROD, List.of("§7Stats that influence what and", "§7how quickly you catch fish."), List.of(
            ItemStatistic.FISHING_SPEED, ItemStatistic.SEA_CREATURE_CHANCE, ItemStatistic.DOUBLE_HOOK_CHANCE,
            ItemStatistic.TROPHY_FISH_CHANCE, ItemStatistic.TREASURE_CHANCE)),
        HUNTING("§eHunting Stats", Material.LEAD, List.of("§7Stats that influence hunting speed", "§7and shard drops."), List.of(
            ItemStatistic.PULL, ItemStatistic.HUNTER_FORTUNE)),
        WISDOM("§3Wisdom Stats", Material.BOOK, List.of("§7Increases the §3XP §7you gain", "§7for your skills."), List.of(
            ItemStatistic.COMBAT_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FISHING_WISDOM,
            ItemStatistic.MINING_WISDOM, ItemStatistic.FORAGING_WISDOM, ItemStatistic.ENCHANTING_WISDOM,
            ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
            ItemStatistic.TAMING_WISDOM, ItemStatistic.SOCIAL_WISDOM, ItemStatistic.HUNTING_WISDOM)),
        MISC("§dMisc Stats", Material.CLOCK, List.of("§7Augments various aspects", "§7of your gameplay."), List.of(
            ItemStatistic.SPEED, ItemStatistic.MAGIC_FIND, ItemStatistic.PET_LUCK, ItemStatistic.HEAT_RESISTANCE,
            ItemStatistic.COLD_RESISTANCE, ItemStatistic.RESPIRATION, ItemStatistic.PRESSURE_RESISTANCE,
            ItemStatistic.FEAR, ItemStatistic.TRACKING));

        private final String title;
        private final Material material;
        private final List<String> description;
        private final List<ItemStatistic> statistics;

        Category(String title, Material material, List<String> description, List<ItemStatistic> statistics) {
            this.title = title;
            this.material = material;
            this.description = description;
            this.statistics = statistics;
        }

        public net.minestom.server.item.ItemStack.Builder createProfileSummary(SkyBlockPlayer player) {
            return createSummary(player, true);
        }

        private net.minestom.server.item.ItemStack.Builder createSummary(SkyBlockPlayer player, boolean clickable) {
            List<String> lore = new ArrayList<>(description);
            lore.add("");
            for (ItemStatistic stat : statistics) {
                lore.add(" " + stat.getFullDisplayName() + " §f" + format(overall(player, stat)) + stat.getSuffix());
            }
            if (clickable) {
                lore.add("");
                lore.add("§eClick for details!");
            }
            return ItemStackCreator.getStack(title, material, 1, lore);
        }
    }
}
