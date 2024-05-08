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
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Objects;

public class GUIBuyBoosterCookies extends SkyBlockInventoryGUI {
    public GUIBuyBoosterCookies() {
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
                    if (slot != 3) {
                        shopCategorys.gui.open(player);
                    }
                }
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (slot != 3) {
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
                    if (slot != 12) {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GRAY_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    } else {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GREEN_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    }
                }
            });
        }
        final Integer cookieCost = 325;
        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (player.getGems() >= cookieCost) {
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    Integer remainingGems = (int) (player.getGems() - cookieCost);
                    player.setGems(remainingGems);
                    new GUIBuyBoosterCookies().open(player);
                } else {
                    player.openBook(Book.builder()
                            .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                                    .appendNewline()
                                    .appendNewline()
                                    .append(Component.text("      "))
                                    .append(Component.text("VISIT STORE").clickEvent(ClickEvent.openUrl("https://store.hypixel.net/nec?ign=" + player.getUsername().toLowerCase())).color(TextColor.fromHexString("#00AAAA"))))
                            .build()
                    );
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                ItemStack.Builder itemStack = ItemStackCreator.enchant(ItemStackCreator.getStack("§6Single Cookie", Material.COOKIE, 1,
                        " ",
                        "§6Booster Cookie §8x1",
                        "§7Consume to gain the §dCookie Buff §7for",
                        "§7§b4 §7days:",
                        " ",
                        "§7▸ Ability to gain §bBits§7!",
                        "§7▸ §3+25☯ §7on all §3Wisdom stats",
                        "§7▸ §b+15✯ §7Magic Find",
                        "§7▸ §7Keep §6coins §7on death",
                        "§7▸ §ePermafly on private islands",
                        "§7▸ §7Quick access to some menus using their respective commands:",
                        "§6/ah§7, §6/bazaar§7, §a/bank§7, §f/anvil§7, §d/etable §7and §e/quiver",
                        "§7▸ Sell items directly to the trades and cookie menu",
                        "§7▸ AFK §aimmunity §7on your island",
                        "§7▸ Toggle specific §dpotion effects",
                        "§8‣ §7Link your items in chat using §e/show",
                        "§8‣ §7Insta-sell your Material stash to the §6Bazaar",
                        " ",
                        "§6Legendary",
                        " ",
                        "§7Cost",
                        "§a" + cookieCost + " Skyblock Gems",
                        " ",
                        "§7You have: §a" + StringUtility.commaify(player.getGems()) + " Gems",
                        ""
                        ));
                ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                if (player.getGems() >= cookieCost) {
                    if (Objects.equals(lore.getLast(), "§eClick here to get gems!")) {
                        lore.removeLast();
                        lore.removeLast();
                        lore.add("§eClick to purchase!");
                    } else {
                        lore.add("§eClick to purchase!");
                    }
                } else {
                    if (Objects.equals(lore.getLast(), "§eClick to purchase!")) {
                        lore.removeLast();
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    } else {
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    }
                }
                return ItemStackCreator.updateLore(itemStack, lore);
            }
        });
        set(new GUIClickableItem(31) {
            final Integer boosterCookieAmount = 6;
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (player.getGems() >= cookieCost*boosterCookieAmount) {
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    Integer remainingGems = player.getGems() - cookieCost*boosterCookieAmount;
                    player.setGems(remainingGems);
                    new GUIBuyBoosterCookies().open(player);
                } else {
                    player.openBook(Book.builder()
                            .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                                    .appendNewline()
                                    .appendNewline()
                                    .append(Component.text("      "))
                                    .append(Component.text("VISIT STORE").clickEvent(ClickEvent.openUrl("http://bit.ly/4aG54lt")).color(TextColor.fromHexString("#00AAAA"))))
                            .build()
                    );
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                ItemStack.Builder itemStack = ItemStackCreator.enchant(ItemStackCreator.getStack("§6Half-Dozen Cookies", Material.COOKIE, 1,
                        " ",
                        "§6Booster Cookie §8x6",
                        "§7Consume to gain the §dCookie Buff §7for",
                        "§7§b4 §7days:",
                        " ",
                        "§7▸ Ability to gain §bBits§7!",
                        "§7▸ §3+25☯ §7on all §3Wisdom stats",
                        "§7▸ §b+15✯ §7Magic Find",
                        "§7▸ §7Keep §6coins §7on death",
                        "§7▸ §ePermafly on private islands",
                        "§7▸ §7Quick access to some menus using their respective commands:",
                        "§6/ah§7, §6/bazaar§7, §a/bank§7, §f/anvil§7, §d/etable §7and §e/quiver",
                        "§7▸ Sell items directly to the trades and cookie menu",
                        "§7▸ AFK §aimmunity §7on your island",
                        "§7▸ Toggle specific §dpotion effects",
                        "§8‣ §7Link your items in chat using §e/show",
                        "§8‣ §7Insta-sell your Material stash to the §6Bazaar",
                        " ",
                        "§6Legendary",
                        " ",
                        "§7Cost",
                        "§a" + cookieCost*boosterCookieAmount + " Skyblock Gems",
                        " ",
                        "§7You have: §a" + StringUtility.commaify(player.getGems()) + " Gems",
                        ""
                ));
                ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                if (player.getGems() >= cookieCost*boosterCookieAmount) {
                    if (Objects.equals(lore.getLast(), "§eClick here to get gems!")) {
                        lore.removeLast();
                        lore.removeLast();
                        lore.add("§eClick to purchase!");
                    } else {
                        lore.add("§eClick to purchase!");
                    }
                } else {
                    if (Objects.equals(lore.getLast(), "§eClick to purchase!")) {
                        lore.removeLast();
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    } else {
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    }
                }
                return ItemStackCreator.updateLore(itemStack, lore);
            }
        });
        set(new GUIClickableItem(33) {
            final Integer boosterCookieAmount = 12;
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (player.getGems() >= cookieCost*boosterCookieAmount) {
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
                    Integer remainingGems = player.getGems() - cookieCost*boosterCookieAmount;
                    player.setGems(remainingGems);
                    new GUIBuyBoosterCookies().open(player);
                } else {
                    player.openBook(Book.builder()
                            .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                                    .appendNewline()
                                    .appendNewline()
                                    .append(Component.text("      "))
                                    .append(Component.text("VISIT STORE").clickEvent(ClickEvent.openUrl("http://bit.ly/4aG54lt")).color(TextColor.fromHexString("#00AAAA"))))
                            .build()
                    );
                }
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                ItemStack.Builder itemStack = ItemStackCreator.enchant(ItemStackCreator.getStack("§6A Dozen Cookies", Material.COOKIE, 1,
                        " ",
                        "§6Booster Cookie §8x12",
                        "§7Consume to gain the §dCookie Buff §7for",
                        "§7§b4 §7days:",
                        " ",
                        "§7▸ Ability to gain §bBits§7!",
                        "§7▸ §3+25☯ §7on all §3Wisdom stats",
                        "§7▸ §b+15✯ §7Magic Find",
                        "§7▸ §7Keep §6coins §7on death",
                        "§7▸ §ePermafly on private islands",
                        "§7▸ §7Quick access to some menus using their respective commands:",
                        "§6/ah§7, §6/bazaar§7, §a/bank§7, §f/anvil§7, §d/etable §7and §e/quiver",
                        "§7▸ Sell items directly to the trades and cookie menu",
                        "§7▸ AFK §aimmunity §7on your island",
                        "§7▸ Toggle specific §dpotion effects",
                        "§8‣ §7Link your items in chat using §e/show",
                        "§8‣ §7Insta-sell your Material stash to the §6Bazaar",
                        " ",
                        "§6Legendary",
                        " ",
                        "§7Cost",
                        "§a" + cookieCost*boosterCookieAmount + " Skyblock Gems",
                        " ",
                        "§7You have: §a" + StringUtility.commaify(player.getGems()) + " Gems",
                        ""
                ));
                ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                if (player.getGems() >= cookieCost*boosterCookieAmount) {
                    if (Objects.equals(lore.getLast(), "§eClick here to get gems!")) {
                        lore.removeLast();
                        lore.removeLast();
                        lore.add("§eClick to purchase!");
                    } else {
                        lore.add("§eClick to purchase!");
                    }
                } else {
                    if (Objects.equals(lore.getLast(), "§eClick to purchase!")) {
                        lore.removeLast();
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    } else {
                        lore.add("§cCannot afford this!");
                        lore.add("§eClick here to get gems!");
                    }
                }
                return ItemStackCreator.updateLore(itemStack, lore);
            }
        });
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
