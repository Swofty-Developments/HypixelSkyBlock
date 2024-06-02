package net.swofty.type.hub.gui.elizabeth;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.hub.gui.elizabeth.subguis.GUIPreviousCityProjects;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Objects;

public class GUICityProjects extends SkyBlockInventoryGUI {
    public GUICityProjects() {
        super("Community Shop", InventoryType.CHEST_6_ROW);
    }

    private final int[] categoriesItemsSlots = {
            10, 11, 12, 13, 14, 16
    };
    private final int[] tabSlots = {
            1, 2, 3, 4, 5, 7
    };

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        GUIAccountAndProfileUpgrades.ShopCategorys[] allShopCategorys = GUIAccountAndProfileUpgrades.ShopCategorys.values();
        int index = 0;
        for (int slot : tabSlots) {
            GUIAccountAndProfileUpgrades.ShopCategorys shopCategorys = allShopCategorys[index];
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (slot != 1) {
                        shopCategorys.gui.open(player);
                    }
                }
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (slot != 1) {
                        ItemStack.Builder itemStack = shopCategorys.stack;
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                        if (Objects.equals(lore.getLast(), "§aCurrently selected!")) {
                            lore.removeLast();
                            lore.add("§eClick to view!");
                        } else if (Objects.equals(lore.getLast(), " ")) {
                            lore.add("§eClick to view!");
                        }
                        return ItemStackCreator.updateLore(itemStack, lore);
                    } else {
                        ItemStack.Builder itemStack = shopCategorys.stack;
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                        if (Objects.equals(lore.getLast(), "§eClick to view!")) {
                            lore.removeLast();
                            lore.add("§aCurrently selected!");
                        } else if (Objects.equals(lore.getLast(), " ")) {
                            lore.add("§aCurrently selected!");
                        }
                        return ItemStackCreator.updateLore(itemStack, lore);
                    }
                }
            });
            index++;
        }

        for (int slot : categoriesItemsSlots) {
            set(new GUIItem(slot) {
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (slot != 10) {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GRAY_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    } else {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GREEN_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    }
                }
            });
        }
        set(new GUIClickableItem(49) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.openBook(Book.builder()
                        .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                                .appendNewline()
                                .appendNewline()
                                .append(Component.text("      "))
                                .append(Component.text("VISIT STORE").clickEvent(ClickEvent.openUrl("http://bit.ly/4aG54lt")).color(TextColor.fromHexString("#00AAAA"))))
                        .build()
                );
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.enchant(ItemStackCreator.getStack("§aCommunity Shop", Material.EMERALD, 1,
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
                        "§eClick to get link!"
                ));
            }
        });
        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIPreviousCityProjects().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aPrevious Projects", Material.BOOKSHELF, 1,
                        "§7The community completed §e7 §7projects",
                        "§7in the past.",
                        " ",
                        "§7You contributed to §b0 §7of those",
                        "§7projects!",
                        " ",
                        "§eClick to view them!");
            }
        });
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
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
