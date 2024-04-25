package net.swofty.type.village.gui.elizabeth;

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
import net.swofty.type.village.gui.elizabeth.subguis.*;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.Objects;

public class GUIBitsShop extends SkyBlockInventoryGUI {

    public GUIBitsShop() {
        super("Community Shop", InventoryType.CHEST_6_ROW);
    }

    private final int[] categoriesItemsSlots = {
            10, 11, 12, 13, 14, 16
    };
    private final int[] tabSlots = {
            1, 2, 3, 4, 5, 7
    };
    private final int[] itemSlots = {
            19, 20,         23, 24, 25,
            28, 29, 30,     32,     34,
                            41, 42, 43
    };
    private final int[] categorySlots = {
                    21, 22,
                        31,     33,
            37, 38, 39, 40
    };

    private enum BitItems {
        GOD_POTION(ItemType.GOD_POTION, 1500),
        KISMET_FEATHER(ItemType.KISMET_FEATHER, 1350),
        MATRIARCHS_PERFUME(ItemType.MATRIARCHS_PERFUME, 1200),
        HOLOGRAM(ItemType.HOLOGRAM, 2000),
        DITTO_BLOB(ItemType.DITTO_BLOB, 600),
        BUILDERS_WAND(ItemType.BUILDERS_WAND, 12000),
        BLOCK_ZAPPER(ItemType.BLOCK_ZAPPER, 5000),
        BITS_TALISMAN(ItemType.BITS_TALISMAN, 15000),
        PORTALIZER(ItemType.PORTALIZER, 4800),
        AUTOPET_RULES_2_PACK(ItemType.AUTOPET_RULES_2_PACK, 21000),
        ;
        private final ItemType item;
        private final Integer price;
        BitItems(ItemType item, Integer price) {
            this.item = item;
            this.price = price;
        }
    }
    private enum SubCategorys {
        KAT_ITEMS(new GUIBitsKatItems(), ItemStackCreator.enchant(ItemStackCreator.getStack("§bKat Items", Material.RED_TULIP, 1,
                "§7Reduce the amount of time it takes",
                "§7to upgrade your pet at §bKat§7."))),
        UPGRADE_COMPONENTS(new GUIBitsUpgradeComponents(), ItemStackCreator.getStackHead("§cUpgrade Components", "59358703ab7727df3324336969e81d6f92b7aa79edb966c0be91ab161bad1f01", 1,
                "§7Upgrade many items in SkyBlock",
                "§7through special crafting",
                "§7components.")),
        SACKS(new GUIBitsSacks(), ItemStackCreator.getStackHead("§5Sacks", "7442c66f4bf9aa4256fa7b49c6367d4658408ec408477879ac8076794402d95b", 1,
                "§7Obtain sack capacity upgrades as well",
                "§7as exclusive bits shop sacks.")),
        ABIPHONE_SUPERSHOP(new GUIBitsAbiphone(), ItemStackCreator.getStackHead("§5Abiphone Supershop", "785d157db6c9fcc1a5bb24c4590988849933bd355608cae3a6a420660676bc33", 1,
                "§7Obtain upgrades and special cases",
                "§7for your Abiphone.",
                " ",
                "§7Purchase an Abiphone in the §cCrimson",
                "§cIsle §7to contact NPCs from afar!")),
        DYES(new GUIBitsDyes(), ItemStackCreator.getStack("§aD§ey§ce§ds", Material.ORANGE_DYE, 1,
                "§7Dyes are exceedingly exclusive items",
                "§7which let you colorize armor pieces.")),
        INFERNO_FUEL_BLOCKS(new GUIBitsInfernoFuel(), ItemStackCreator.getStackHead("§9Inferno Fuel Blocks", "28a1884ee3f8a6e66692a91ed763cb78d9f2017706d8b42a9263b417b2d715d2", 1,
                "§7Use fuel blocks when creating",
                "§6Inferno §7minion fuel and level up",
                "§7your §cChili Pepper §7collection!")),
        STACKING_ENCHANTS(new GUIBitsStackingEnchants(), ItemStackCreator.getStack("§9Stacking Enchants", Material.ENCHANTED_BOOK, 1,
                "§7Unlock unique §9enchants §7to apply",
                "§7on your gear.",
                " ",
                "§7Stacking enchants become",
                "§7stronger as you use the gear it's",
                "§7on.")),
        ENRICHMENTS(new GUIBitsEnrichments(), ItemStackCreator.getStackHead("§dEnrichments", "32fa8f38c7b22096619c3a6d6498b405530e48d5d4f91e2aacea578844d5c67", 1,
                "§7Add a §dboost §7of a stat of your choice",
                "§7to your accessories.",
                " ",
                "§7Only one enrichment may be",
                "§7applied per item.")),
        ;
        private final SkyBlockInventoryGUI gui;
        private final ItemStack.Builder item;

        SubCategorys(SkyBlockInventoryGUI gui, ItemStack.Builder item) {

            this.gui = gui;
            this.item = item;
        }
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(15, ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));

        GUIAccountAndProfileUpgrades.ShopCategorys[] allShopCategorys = GUIAccountAndProfileUpgrades.ShopCategorys.values();
        int index = 0;
        for (int slot : tabSlots) {
            GUIAccountAndProfileUpgrades.ShopCategorys shopCategorys = allShopCategorys[index];
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (slot != 4) {
                        shopCategorys.gui.open(player);
                    }
                }
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    if (slot != 4) {
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
                    if (slot != 13) {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GRAY_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    } else {
                        return ItemStackCreator.getStack("§8▲ §7Categories", Material.GREEN_STAINED_GLASS_PANE, 1, "§8▼ §7Items");
                    }
                }
            });
        }
        BitItems[] allBitItems = BitItems.values();
        int index2 = 0;
        for (int slot : itemSlots) {
            if (index2 + 1 <= BitItems.values().length) {
                BitItems bitItems = allBitItems[index2];
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (player.getBits() >= bitItems.price) {
                            if (!player.getPurchaseConfirmationBits()) {
                                player.addAndUpdateItem(bitItems.item);
                                Integer remainingBits = player.getBits() - bitItems.price;
                                player.setBits(remainingBits);
                                new GUIBitsShop().open(player);
                            } else {
                                new GUIBitsConfirmBuy(bitItems.item, bitItems.price).open(player);
                            }
                        } else {
                            player.sendMessage("§cYou don't have enough Bits to buy that!");
                        }
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        SkyBlockItem item = new SkyBlockItem(bitItems.item);
                        ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                        lore.add(" ");
                        lore.add("§7Cost");
                        lore.add("§b" + StringUtility.commaify(bitItems.price) + " Bits");
                        lore.add(" ");
                        lore.add("§eClick to trade!");
                        return ItemStackCreator.updateLore(itemStack, lore);
                    }
                });
                index2++;
            }
        }
        SubCategorys[] allSubCategorys = SubCategorys.values();
        int index3 = 0;
        for (int slot : categorySlots) {
            SubCategorys subCategorys = allSubCategorys[index3];
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    subCategorys.gui.open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ItemStack.Builder itemstack = subCategorys.item;
                    ArrayList<String> lore = new ArrayList<>(itemstack.build().getLore().stream().map(StringUtility::getTextFromComponent).toList());
                    if (!Objects.equals(lore.getLast(), "§eClick to browse!")) {
                        lore.add(" ");
                        lore.add("§eClick to browse!");
                    }
                    return ItemStackCreator.updateLore(itemstack, lore);
                }
            });
            index3++;
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
        set(new GUIClickableItem(48) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.setPurchaseConfirmationBits(!player.getPurchaseConfirmationBits());
                new GUIBitsShop().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                String status;
                if (player.getPurchaseConfirmationBits()) {
                    status = "§aEnabled!";
                } else {
                    status = "§cOFF";
                }
                return ItemStackCreator.getStack("§aPurchase Confirmation", Material.COMPARATOR, 1,
                        "§7Buying a lot and never",
                        "§7second-guess a decision?",
                        " ",
                        "§7Confirmations: " + status,
                        " ",
                        "§eClick to toggle confirm menu!");
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
