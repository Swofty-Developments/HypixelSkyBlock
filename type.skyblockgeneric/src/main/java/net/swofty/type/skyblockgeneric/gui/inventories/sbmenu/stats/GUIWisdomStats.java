package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.stats;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockProfile;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.user.statistics.PlayerStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIWisdomStats extends HypixelInventoryGUI {

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

    public GUIWisdomStats() {
        super("Your Stats Breakdown", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        //set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockProfile()));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7Increases the §3XP §7you gain on your", "§7skills ", " "));
                List<ItemStatistic> stats = new ArrayList<>(List.of(ItemStatistic.COMBAT_WISDOM, ItemStatistic.MINING_WISDOM, ItemStatistic.FARMING_WISDOM, ItemStatistic.FORAGING_WISDOM,
                        ItemStatistic.FISHING_WISDOM, ItemStatistic.ENCHANTING_WISDOM, ItemStatistic.ALCHEMY_WISDOM, ItemStatistic.CARPENTRY_WISDOM, ItemStatistic.RUNE_CRAFTING_WISDOM,
                        ItemStatistic.SOCIAL_WISDOM, ItemStatistic.TAMING_WISDOM, ItemStatistic.HUNTING_WISDOM
                )); // WISDOM STATS
                statistics.allStatistics().getOverall().forEach((statistic, value) -> {
                    if (stats.contains(statistic)) {
                        lore.add(" " + statistic.getFullDisplayName() + " §f" +
                                StringUtility.decimalify(value, 2) + statistic.getSuffix());
                    }
                });

                return ItemStackCreator.getStack("§3Wisdom Stats", Material.BOOK, 1, lore);
            }
        });

        for (Map.Entry<Integer, ItemStatistic> entry : displaySlots.entrySet()) {
            set(new GUIClickableItem(entry.getKey()) {

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    ItemStatistic statistic = entry.getValue();
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
                }

                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                    player.sendMessage("§aUnder construction!");
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(false);
    }
}
