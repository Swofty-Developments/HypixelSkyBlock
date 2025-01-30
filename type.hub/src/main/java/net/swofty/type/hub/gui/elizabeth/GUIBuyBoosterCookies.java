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
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Objects;

public class GUIBuyBoosterCookies extends SkyBlockAbstractInventory {
    private static final String STATE_TAB_PREFIX = "tab_";
    private static final String STATE_CATEGORY_PREFIX = "category_";
    private static final String STATE_CAN_AFFORD_PREFIX = "can_afford_";

    private static final int COOKIE_COST = 325;
    private final int[] categoriesItemsSlots = {10, 11, 12, 13, 14, 16};
    private final int[] tabSlots = {1, 2, 3, 4, 5, 7};

    public GUIBuyBoosterCookies() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Community Shop")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        doAction(new AddStateAction(STATE_TAB_PREFIX + "3")); // Set initial tab state
        doAction(new AddStateAction(STATE_CATEGORY_PREFIX + "12")); // Set initial category state

        setupTabs();
        setupCategories();
        setupCookieOptions(player);
        setupCommunityShopButton();
    }

    private void setupTabs() {
        GUIAccountAndProfileUpgrades.ShopCategories[] allShopCategories = GUIAccountAndProfileUpgrades.ShopCategories.values();

        for (int i = 0; i < tabSlots.length; i++) {
            final int slot = tabSlots[i];
            final GUIAccountAndProfileUpgrades.ShopCategories category = allShopCategories[i];

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ItemStack.Builder itemStack = category.stack;
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                                .stream().map(StringUtility::getTextFromComponent).toList());

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
                        if (slot != 3) {
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
                    .item(() -> ItemStackCreator.getStack("§8▲ §7Categories",
                            slot != 12 ? Material.GRAY_STAINED_GLASS_PANE : Material.GREEN_STAINED_GLASS_PANE,
                            1, "§8▼ §7Items").build())
                    .build());
        }
    }

    private void setupCookieOptions(SkyBlockPlayer player) {
        setupCookieOption(29, "§6Single Cookie", 1, player);
        setupCookieOption(31, "§6Half-Dozen Cookies", 6, player);
        setupCookieOption(33, "§6A Dozen Cookies", 12, player);
    }

    private void setupCookieOption(int slot, String name, int amount, SkyBlockPlayer player) {
        String stateId = STATE_CAN_AFFORD_PREFIX + slot;
        if (player.getGems() >= COOKIE_COST * amount) {
            doAction(new AddStateAction(stateId));
        }

        attachItem(GUIItem.builder(slot)
                .item(() -> createCookieItemStack(name, amount, player))
                .onClick((ctx, item) -> {
                    if (hasState(stateId)) {
                        giveCookies(ctx.player(), amount);
                        return true;
                    } else {
                        openStoreBook(ctx.player());
                        return false;
                    }
                })
                .build());
    }

    private ItemStack createCookieItemStack(String name, int amount, SkyBlockPlayer player) {
        ItemStack.Builder itemStack = ItemStackCreator.enchant(ItemStackCreator.getStack(name, Material.COOKIE, 1,
                createCookieLore(amount, player)));

        ArrayList<String> lore = new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                .stream().map(StringUtility::getTextFromComponent).toList());

        boolean canAfford = player.getGems() >= COOKIE_COST * amount;
        if (canAfford) {
            if (Objects.equals(lore.getLast(), "§eClick here to get gems!")) {
                lore.removeLast();
                lore.removeLast();
                lore.add("§eClick to purchase!");
            } else if (!Objects.equals(lore.getLast(), "§eClick to purchase!")) {
                lore.add("§eClick to purchase!");
            }
        } else {
            if (Objects.equals(lore.getLast(), "§eClick to purchase!")) {
                lore.removeLast();
                lore.add("§cCannot afford this!");
                lore.add("§eClick here to get gems!");
            } else if (!Objects.equals(lore.getLast(), "§eClick here to get gems!")) {
                lore.add("§cCannot afford this!");
                lore.add("§eClick here to get gems!");
            }
        }

        return ItemStackCreator.updateLore(itemStack, lore).build();
    }

    private String[] createCookieLore(int amount, SkyBlockPlayer player) {
        return new String[]{
                " ",
                "§6Booster Cookie §8x" + amount,
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
                "§a" + COOKIE_COST * amount + " Skyblock Gems",
                " ",
                "§7You have: §a" + StringUtility.commaify(player.getGems()) + " Gems",
                ""
        };
    }

    private void giveCookies(SkyBlockPlayer player, int amount) {
        for (int i = 0; i < amount; i++) {
            player.addAndUpdateItem(ItemType.BOOSTER_COOKIE);
        }
        player.setGems(player.getGems() - COOKIE_COST * amount);
        player.openInventory(new GUIBuyBoosterCookies());
    }

    private void setupCommunityShopButton() {
        attachItem(GUIItem.builder(49)
                .item(() -> ItemStackCreator.enchant(ItemStackCreator.getStack("§aCommunity Shop", Material.EMERALD, 1,
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
                    openStoreBook(ctx.player());
                    return true;
                })
                .build());
    }

    private void openStoreBook(SkyBlockPlayer player) {
        player.openBook(Book.builder()
                .addPage(Component.text("Purchase ranks, gems and more on our webstore!")
                        .appendNewline()
                        .appendNewline()
                        .append(Component.text("      "))
                        .append(Component.text("VISIT STORE")
                                .clickEvent(ClickEvent.openUrl("http://bit.ly/4aG54lt"))
                                .color(TextColor.fromHexString("#00AAAA"))))
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