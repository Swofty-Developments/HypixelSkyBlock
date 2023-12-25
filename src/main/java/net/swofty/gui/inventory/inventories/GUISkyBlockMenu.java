package net.swofty.gui.inventory.inventories;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.items.miscellaneous.SkyBlockMenu;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.statistics.PlayerStatistics;
import net.swofty.utility.StringUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GUISkyBlockMenu extends SkyBlockInventoryGUI {
    public GUISkyBlockMenu() {
        super("SkyBlock Menu", InventoryType.CHEST_6_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.sendMessage("§cNot yet implemented");
            }

            @Override
            public int getSlot() {
                return 13;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                PlayerStatistics statistics = player.getStatistics();
                List<String> lore = new ArrayList<>(List.of("§7View your equipment, stats, and more!", "§e "));

                statistics.allStatistics().getStatistics().forEach((statistic, value) -> {
                    if (statistic.getColour() != null)
                        lore.add(" " + statistic.getColour() + statistic.getSymbol() + " " +
                                StringUtility.toNormalCase(statistic.name()) + " §f" +
                                value + statistic.getSuffix());
                });

                lore.add("§e ");
                lore.add("§eClick to view!");

                return ItemStackCreator.getStackHead("§aYour SkyBlock Profile",
                        PlayerSkin.fromUuid(player.getUuid().toString()), 1,
                        lore);
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUICrafting().open(player);
            }

            @Override
            public int getSlot() {
                return 31;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCrafting Table", Material.CRAFTING_TABLE, (short) 0, 1,
                        "§7Opens the crafting grid.",
                        " ",
                        "§eClick to open!");
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
