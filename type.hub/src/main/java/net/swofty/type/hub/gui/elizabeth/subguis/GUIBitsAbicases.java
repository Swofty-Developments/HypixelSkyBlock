package net.swofty.type.hub.gui.elizabeth.subguis;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.ItemType;
import net.swofty.type.hub.gui.elizabeth.CommunityShopItem;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIBitsAbicases extends SkyBlockAbstractInventory {
    private static final int[] DISPLAY_SLOTS = {11, 13, 15};

    public GUIBitsAbicases() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Bits Shop - Abicases")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Bits Shop - Abiphone").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBitsAbiphone());
                    return true;
                })
                .build());

        // Setup subcategories
        SubCategories[] allSubCategories = SubCategories.values();
        for (int i = 0; i < DISPLAY_SLOTS.length; i++) {
            SubCategories category = allSubCategories[i];
            setupSubcategoryItem(DISPLAY_SLOTS[i], category);
        }
    }

    private void setupSubcategoryItem(int slot, SubCategories category) {
        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder itemstack = category.item;
                    ArrayList<String> lore = new ArrayList<>(itemstack.build()
                            .get(ItemComponent.LORE).stream()
                            .map(StringUtility::getTextFromComponent)
                            .toList());

                    if (!Objects.equals(lore.get(lore.size() - 1), "§eClick to browse!")) {
                        lore.add(" ");
                        lore.add("§eClick to browse!");
                    }
                    return ItemStackCreator.updateLore(itemstack, lore).build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBitsSubCategories(
                            category.getShopItems(),
                            category.getGuiName(),
                            category.getPreviousGUI()));
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }

    @Getter
    private enum SubCategories {
        SUMSUNG_ABICASES("Sumsung© Abicases", new GUIBitsAbicases(),
                ItemStackCreator.getStackHead("§fSumsung© Abicases",
                        "36a10ee2155fc0134d9392000a9eb9ebcba8526eff3893e54434e825e558fb55", 1,
                        "§7Sumsung focuses on the",
                        "§7technology.",
                        " ",
                        "§7Upgrade your Abiphone remotely",
                        "§7through cloud-based blockchain",
                        "§7agile immutable dev-ops fuzzy",
                        "§7cases.",
                        " ",
                        "§eClick to view models!"),
                List.of(
                        new CommunityShopItem(ItemType.SUMSUNG_G3_ABICASE, 15000, 1),
                        new CommunityShopItem(ItemType.SUMSUNG_GG_ABICASE, 25000, 1)
                )),
        REZAR_ABICASES("Rezar Abicase", new GUIBitsAbicases(),
                ItemStackCreator.getStackHead("§aRezar® Abicase",
                        "b2128f48d997186563fbc5b47a88c0d0aac92fa2c285cd1fae420c34fa8f2010", 1,
                        "§7Play hard, play fair and do it all in",
                        "§7green.",
                        " ",
                        "§7Perfect for those who have time",
                        "§7to grind but no time to call their",
                        "§7close ones.",
                        " ",
                        "§eClick to view THE model!"),
                List.of(
                        new CommunityShopItem(ItemType.REZAR_ABICASE, 26000, 1)
                )),
        BLUE_ABICASES("Blue™ Abicases", new GUIBitsAbicases(),
                ItemStackCreator.getStackHead("§9Blue™ Abicases",
                        "a3c153c391c34e2d328a60839e683a9f82ad3048299d8bc6a39e6f915cc5a", 1,
                        "§7Blue Abicases are not all blue.",
                        "§7Their color reflects your",
                        "§7personality, your life and your",
                        "§7legacy.",
                        " ",
                        "§7Think of it this way: Is your",
                        "§7personality just a recolor of last",
                        "§7year's?",
                        " ",
                        "§7Blue™ says... well §omaybe§7!",
                        " ",
                        "§eClick to pick a color!"),
                List.of(
                        new CommunityShopItem(ItemType.BLUE_BUT_RED_ABICASE, 17000, 1),
                        new CommunityShopItem(ItemType.ACTUALLY_BLUE_ABICASE, 17000, 1),
                        new CommunityShopItem(ItemType.BLUE_BUT_GREEN_ABICASE, 17000, 1),
                        new CommunityShopItem(ItemType.BLUE_BUT_YELLOW_ABICASE, 17000, 1),
                        new CommunityShopItem(ItemType.LIGHTER_BLUE_ABICASE, 17000, 1)
                ));

        private final String guiName;
        private final SkyBlockAbstractInventory previousGUI;
        private final ItemStack.Builder item;
        private final List<CommunityShopItem> shopItems;

        SubCategories(String guiName, SkyBlockAbstractInventory previousGUI, ItemStack.Builder item,
                      List<CommunityShopItem> shopItems) {
            this.guiName = guiName;
            this.previousGUI = previousGUI;
            this.item = item;
            this.shopItems = shopItems;
        }
    }
}