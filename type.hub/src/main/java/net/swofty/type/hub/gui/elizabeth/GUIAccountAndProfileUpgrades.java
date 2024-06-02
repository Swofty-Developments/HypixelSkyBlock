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
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Objects;

public class GUIAccountAndProfileUpgrades extends SkyBlockInventoryGUI {

    public GUIAccountAndProfileUpgrades() {
        super("Community Shop", InventoryType.CHEST_6_ROW);
    }

    private final int[] categoriesItemsSlots = {
            10, 11, 12, 13, 14, 16
    };
    private final int[] tabSlots = {
            1, 2, 3, 4, 5, 7
    };
    public enum ShopCategorys {
        CITY_PROJECTS(new GUICityProjects(), ItemStackCreator.getStack("§aCity Projects", Material.GOLDEN_HORSE_ARMOR, 1,
                "§7Participate with the whole SkyBlock",
                "§7community to upgrade the village",
                "§7and more.",
                " ",
                "§7§bContribute §7to various projects to",
                "§7obtain unique perks!",
                " ")),
        ACCOUNT_AND_PROFILE_UPGRADES(new GUIAccountAndProfileUpgrades(), ItemStackCreator.getStack("§dAccount & Profile Upgrades", Material.HOPPER, 1,
                "§7Upgrade your current profile and your",
                "§7SkyBlock account with permanent",
                "§7upgrades.",
                " ",
                "§7Profile: §8Nothing going on!",
                "§7Account: §8None underway!",
                " ")),
        BOOSTER_COOKIE(new GUIBuyBoosterCookies(), ItemStackCreator.enchant(ItemStackCreator.getStack("§6Booster Cookie", Material.COOKIE, 1,
                "§7Obtain a temporary buff letting",
                "§7you earn §bbits§7, as well as §dtons of",
                "§dperks§7.",
                " "))),
        BITS_SHOP(new GUIBitsShop(), ItemStackCreator.enchant(ItemStackCreator.getStack("§bBits Shop", Material.DIAMOND, 1,
                "§7Spend §bbits §7on a variety of",
                "§7powerful items.",
                " ",
                "§7Earn bits from §6Booster Cookies§7.",
                " "))),
        FIRE_SALES(new GUIFIRESales(), ItemStackCreator.enchant(ItemStackCreator.getStack("§6♨ §c§lFIRE §cSales §6♨", Material.BLAZE_POWDER, 1,
                "§7Acquire §6exclusive §7cosmetics which are",
                "§7only available in §climited quantity",
                "§c§7across all of SkyBlock.",
                " ",
                "§6§lUPCOMING SALE",
                "§c§l0 §7Fire Sales are starting soon.",
                " "))),
        HYPIXEL_RANKS(new GUIHypixelRanks(), ItemStackCreator.enchant(ItemStackCreator.getStack("§eHypixel Ranks", Material.EMERALD, 1,
                "§7Browse the SkyBlock perks of our",
                "§7§eserver-wide §7ranks such as the",
                "§7§6[MVP§2++§6] §7rank.",
                " "))),
        ;
        public final SkyBlockInventoryGUI gui;
        public final ItemStack.Builder stack;

        ShopCategorys(SkyBlockInventoryGUI gui, ItemStack.Builder stack) {
            this.gui = gui;
            this.stack = stack;
        }
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        ShopCategorys[] allShopCategorys = ShopCategorys.values();
        int index = 0;
        for (int slot : tabSlots) {
            ShopCategorys shopCategorys = allShopCategorys[index];
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (slot != 2) {
                        shopCategorys.gui.open(player);
                    }
                }
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (slot != 2) {
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
                    if (slot != 11) {
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
