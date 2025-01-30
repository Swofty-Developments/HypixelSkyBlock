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
import net.swofty.type.hub.gui.elizabeth.subguis.GUIPreviousCityProjects;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Objects;

public class GUICityProjects extends SkyBlockAbstractInventory {
    private static final String STATE_TAB_PREFIX = "tab_";
    private static final String STATE_CATEGORY_PREFIX = "category_";

    private final int[] categoriesItemsSlots = {
            10, 11, 12, 13, 14, 16
    };
    private final int[] tabSlots = {
            1, 2, 3, 4, 5, 7
    };

    public GUICityProjects() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Community Shop")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        doAction(new AddStateAction(STATE_TAB_PREFIX + "1")); // Set initial tab state
        doAction(new AddStateAction(STATE_CATEGORY_PREFIX + "10")); // Set initial category state

        setupTabs();
        setupCategories();
        setupCommunityShopButton();
        setupPreviousProjectsButton();
    }

    private void setupTabs() {
        GUIAccountAndProfileUpgrades.ShopCategories[] allShopCategories = GUIAccountAndProfileUpgrades.ShopCategories.values();

        for (int i = 0; i < tabSlots.length; i++) {
            final int slot = tabSlots[i];
            final GUIAccountAndProfileUpgrades.ShopCategories category = allShopCategories[i];
            final int index = i;

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ItemStack.Builder itemStack = category.stack;
                        ArrayList<String> lore = new ArrayList<>(itemStack.build()
                                .get(ItemComponent.LORE).stream()
                                .map(StringUtility::getTextFromComponent)
                                .toList());

                        if (hasState(STATE_TAB_PREFIX + slot)) {
                            if (!Objects.equals(lore.getLast(), "§aCurrently selected!")) {
                                if (Objects.equals(lore.getLast(), "§eClick to view!")) {
                                    lore.removeLast();
                                }
                                lore.add("§aCurrently selected!");
                            }
                        } else {
                            if (!Objects.equals(lore.getLast(), "§eClick to view!")) {
                                if (Objects.equals(lore.getLast(), "§aCurrently selected!")) {
                                    lore.removeLast();
                                }
                                lore.add("§eClick to view!");
                            }
                        }

                        return ItemStackCreator.updateLore(itemStack, lore).build();
                    })
                    .onClick((ctx, item) -> {
                        if (slot != 1) {
                            ctx.player().openInventory(category.gui);
                        }
                        return true;
                    })
                    .build());
        }
    }

    private void setupCategories() {
        for (int slot : categoriesItemsSlots) {
            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        if (slot != 10) {
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

    private void setupCommunityShopButton() {
        attachItem(GUIItem.builder(49)
                .item(() -> ItemStackCreator.enchant(ItemStackCreator.getStack("§aCommunity Shop",
                        Material.EMERALD, 1,
                        "§8Elizabeth",
                        " ",
                        "§7Gems: §a" + StringUtility.commaify(owner.getGems()),
                        "§8Purchase on store.hypixel.net!",
                        " ",
                        "§7Bits: §b" + StringUtility.commaify(owner.getBits()),
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

    private void setupPreviousProjectsButton() {
        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.getStack("§aPrevious Projects",
                        Material.BOOKSHELF, 1,
                        "§7The community completed §e7 §7projects",
                        "§7in the past.",
                        " ",
                        "§7You contributed to §b0 §7of those",
                        "§7projects!",
                        " ",
                        "§eClick to view them!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIPreviousCityProjects());
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {}

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}