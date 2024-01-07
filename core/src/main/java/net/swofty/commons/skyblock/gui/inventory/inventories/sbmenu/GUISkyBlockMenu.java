package net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu;

import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu.collection.GUICollections;
import net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu.profiles.GUIProfileManagement;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;
import net.swofty.commons.skyblock.gui.inventory.ItemStackCreator;
import net.swofty.commons.skyblock.gui.inventory.inventories.sbmenu.crafting.GUIRecipeBook;
import net.swofty.commons.skyblock.gui.inventory.item.GUIClickableItem;
import net.swofty.commons.skyblock.item.impl.SkyBlockRecipe;
import net.swofty.commons.skyblock.user.statistics.PlayerStatistics;
import net.swofty.commons.skyblock.utility.StringUtility;

import java.util.ArrayList;
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
                new GUIRecipeBook().open(player);
            }

            @Override
            public int getSlot() {
                return 21;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7Through your adventure, you will",
                        "§7unlock recipes for all kinds of",
                        "§7special items! You can view how to",
                        "§7craft these items here.",
                        " "
                ));

                SkyBlockRecipe.getMissionDisplay(lore, player.getUuid());

                lore.add(" ");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§aRecipe Book", Material.BOOK, (short) 0, 1, lore);
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIMissionLog().open(player);
            }

            @Override
            public int getSlot() {
                return 23;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aQuest Log", Material.WRITABLE_BOOK, (short) 0, 1,
                        "§7View your active quests, progress",
                        "§7and rewards.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUICollections().open(player);
            }

            @Override
            public int getSlot() {
                return 20;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7View all of the items available in",
                        "§7SkyBlock. Collect more of an item to",
                        "§7unlock rewards on your way to",
                        "§7becoming a master of SkyBlock!",
                        " "
                ));

                player.getCollection().getDisplay(lore);

                lore.add(" ");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1, lore.toArray(new String[0]));
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

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIProfileManagement().open(player);
            }

            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aProfile Management", Material.NAME_TAG, (short) 0, 1,
                        "§7You can have multiple SkyBlock",
                        "§7profiles at the same time.",
                        " ",
                        "§7Each profile has its own island,",
                        "§7inventory, quest log...",
                        " ",
                        "§7Profiles: §e" + player.getProfiles().getProfiles().size() + "§6/§e4",
                        " ",
                        "§eClick to manage!");
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
