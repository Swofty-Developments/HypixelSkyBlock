package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.v2.*;
import net.swofty.type.generic.gui.v2.context.ViewContext;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIWisdomStats extends StatelessView {

    private static final Map<Integer, ItemStatistic> displaySlots = Map.ofEntries(
            Map.entry(10, ItemStatistic.COMBAT_WISDOM),
            Map.entry(11, ItemStatistic.MINING_WISDOM),
            Map.entry(12, ItemStatistic.FARMING_WISDOM),
            Map.entry(13, ItemStatistic.FORAGING_WISDOM),
            Map.entry(14, ItemStatistic.FISHING_WISDOM),
            Map.entry(15, ItemStatistic.ENCHANTING_WISDOM),
            Map.entry(16, ItemStatistic.ALCHEMY_WISDOM),
            Map.entry(19, ItemStatistic.CARPENTRY_WISDOM),
            Map.entry(20, ItemStatistic.RUNE_CRAFTING_WISDOM),
            Map.entry(21, ItemStatistic.SOCIAL_WISDOM),
            Map.entry(22, ItemStatistic.TAMING_WISDOM),
            Map.entry(23, ItemStatistic.HUNTING_WISDOM)
    );

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Your Stats Breakdown", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);

        layout.slot(4, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            PlayerStatistics statistics = player.getStatistics();
            List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills ", " "));
            List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.COMBAT_WISDOM, ItemStatistic.MINING_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FORAGING_WISDOM,
                    ItemStatistic.FISHING_WISDOM, ItemStatistic.ENCHANTING_WISDOM, ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
                    ItemStatistic.SOCIAL_WISDOM, ItemStatistic.TAMING_WISDOM, ItemStatistic.HUNTING_WISDOM
            ));
            statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                if (stats.contains(statistic)) {
                    lore.add(" " + statistic.getFullDisplayName() + " §f" +
                            StringUtility.decimalify(value, 2) + statistic.getSuffix());
                }
            });

            return ItemStackCreator.getStack("§3Wisdom Stats", Material.BOOK, 1, lore);
        });

        for (Map.Entry<Integer, ItemStatistic> entry : displaySlots.entrySet()) {
            ItemStatistic statistic = entry.getValue();

            layout.slot(entry.getKey(), (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                double value = player.getStatistics().allStatistics().getOverall(statistic);
                double multiplier = 1D + value / 100D;
                List<String> lore = new ArrayList<>();

                lore.add("§7" + statistic.getDisplayName() + " increases how much");
                lore.add("§7" + statistic.getDisplayName().split(" ")[0] + " Skill XP that you gain.");
                lore.add(" ");

                if (value == 0D) {
                    lore.add("§8You aren't learning any faster, yet!");
                } else {
                    lore.add("§7XP Multiplier: " + statistic.getDisplayColor()
                            + StringUtility.decimalify(multiplier, 2) + "x");
                }

                lore.add(" ");

                if (value == 0D) lore.add("§8You have none of this stat!");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack(statistic.getFullDisplayName() + " §f" +
                                StringUtility.decimalify(value, 1),
                        Material.WRITABLE_BOOK, 1, lore
                );
            }, (click, c) -> {
                ((SkyBlockPlayer) c.player()).sendMessage("§aUnder construction!");
            });
        }
    }

    @Override
    public boolean onBottomClick(net.swofty.type.generic.gui.v2.context.ClickContext<DefaultState> click, ViewContext ctx) {
        return true;
    }
}
