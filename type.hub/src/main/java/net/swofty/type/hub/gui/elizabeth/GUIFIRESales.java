package net.swofty.type.hub.gui.elizabeth;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Objects;

public class GUIFIRESales extends SkyBlockAbstractInventory {
    private static final int[] CATEGORIES_ITEMS_SLOTS = {
            10, 11, 12, 13, 14, 16
    };
    private static final int[] TAB_SLOTS = {
            1, 2, 3, 4, 5, 7
    };

    public GUIFIRESales() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Community Shop")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Set initial state for the selected tab
        doAction(new AddStateAction("tab_selected_5"));

        setupTabs(player);
        setupCategoryDividers();
        setupShopButton(player);
    }

    private void setupTabs(SkyBlockPlayer player) {
        GUIAccountAndProfileUpgrades.ShopCategories[] allShopCategories = GUIAccountAndProfileUpgrades.ShopCategories.values();

        int index = 0;
        for (int slot : TAB_SLOTS) {
            final int finalIndex = index;
            GUIAccountAndProfileUpgrades.ShopCategories category = allShopCategories[index];

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ItemStack.Builder itemStack = category.stack;
                        ArrayList<String> lore = new ArrayList<>(itemStack.build()
                                .get(ItemComponent.LORE)
                                .stream()
                                .map(StringUtility::getTextFromComponent)
                                .toList());

                        boolean isSelected = hasState("tab_selected_" + slot);

                        if (Objects.equals(lore.getLast(), "§aCurrently selected!")) {
                            lore.removeLast();
                            if (!isSelected) {
                                lore.add("§eClick to view!");
                            }
                        } else if (Objects.equals(lore.getLast(), "§eClick to view!")) {
                            lore.removeLast();
                            if (isSelected) {
                                lore.add("§aCurrently selected!");
                            } else {
                                lore.add("§eClick to view!");
                            }
                        } else if (Objects.equals(lore.getLast(), " ")) {
                            lore.add(isSelected ? "§aCurrently selected!" : "§eClick to view!");
                        }

                        return ItemStackCreator.updateLore(itemStack, lore).build();
                    })
                    .onClick((ctx, item) -> {
                        if (slot != 5) {
                            ctx.player().openInventory(category.gui);
                        }
                        return true;
                    })
                    .build());

            index++;
        }
    }

    private void setupCategoryDividers() {
        for (int slot : CATEGORIES_ITEMS_SLOTS) {
            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        if (slot != 14) {
                            return ItemStackCreator.getStack("§8▲ §7Categories",
                                    Material.GRAY_STAINED_GLASS_PANE, 1,
                                    "§8▼ §7Items").build();
                        } else {
                            return ItemStackCreator.getStack("§8▲ §7Categories",
                                    Material.GREEN_STAINED_GLASS_PANE, 1,
                                    "§8▼ §7Items").build();
                        }
                    })
                    .build());
        }
    }

    private void setupShopButton(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(49)
                .item(() -> ItemStackCreator.enchant(ItemStackCreator.getStack("§aCommunity Shop",
                        Material.EMERALD, 1,
                        "§8Elizabeth",
                        " ",
                        "§7Gems: §a" + StringUtility.commaify(player.getGems()),
                        "§8Purchase on store.hypixel.net!",
                        " ",
                        "§7Bits: §b" + StringUtility.commaify(player.getBits()),
                        "§8Earn from Booster Cookies!",
                        " ",
                        "§7Fame Rank: §e",
                        "§8Rank up by spending gems & bits!",
                        "§eClick to get link!")).build())
                .onClick((ctx, item) -> {
                    ctx.player().openBook(Book.builder()
                            .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                                    .appendNewline()
                                    .appendNewline()
                                    .append(Component.text("      "))
                                    .append(Component.text("VISIT STORE")
                                            .clickEvent(ClickEvent.openUrl("http://bit.ly/4aG54lt"))
                                            .color(TextColor.fromHexString("#00AAAA"))))
                            .build());
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
}