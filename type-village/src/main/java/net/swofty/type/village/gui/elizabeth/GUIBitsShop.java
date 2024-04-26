package net.swofty.type.village.gui.elizabeth;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
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

import java.util.*;

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
                        31,
            37, 38, 39, 40
    };

    private enum BitItems {
        GOD_POTION(ItemType.GOD_POTION, 1500, 1),
        KISMET_FEATHER(ItemType.KISMET_FEATHER, 1350, 1),
        MATRIARCHS_PERFUME(ItemType.MATRIARCHS_PERFUME, 1200, 1),
        HOLOGRAM(ItemType.HOLOGRAM, 2000, 1),
        DITTO_BLOB(ItemType.DITTO_BLOB, 600, 1),
        BUILDERS_WAND(ItemType.BUILDERS_WAND, 12000, 1),
        BLOCK_ZAPPER(ItemType.BLOCK_ZAPPER, 5000, 1),
        BITS_TALISMAN(ItemType.BITS_TALISMAN, 15000, 1),
        PORTALIZER(ItemType.PORTALIZER, 4800, 1),
        AUTOPET_RULES_2_PACK(ItemType.AUTOPET_RULES_2_PACK, 21000, 1),
        ;
        private final ItemType item;
        private final Integer price;
        private final Integer amount;
        BitItems(ItemType item, Integer price, Integer amount) {
            this.item = item;
            this.price = price;
            this.amount = amount;
        }
    }
    private enum SubCategorys {
        KAT_ITEMS("Kat Items", new GUIBitsShop(), ItemStackCreator.enchant(ItemStackCreator.getStack("§bKat Items", Material.RED_TULIP, 1,
                "§7Reduce the amount of time it takes",
                "§7to upgrade your pet at §bKat§7.")),
                Map.of(ItemType.KAT_FLOWER, Map.entry(500, 1)),
                Map.of(ItemType.KAT_BOUQUET, Map.entry(2500, 1))),
        UPGRADE_COMPONENTS("Upgrade Components", new GUIBitsShop(), ItemStackCreator.getStackHead("§cUpgrade Components", "59358703ab7727df3324336969e81d6f92b7aa79edb966c0be91ab161bad1f01", 1,
                "§7Upgrade many items in SkyBlock",
                "§7through special crafting",
                "§7components."),
                Map.of(ItemType.HEAT_CORE, Map.entry(3000, 1)),
                Map.of(ItemType.HYPER_CATALYST_UPGRADER, Map.entry(300, 1)),
                Map.of(ItemType.ULTIMATE_CARROT_CANDY_UPGRADE, Map.entry(8000, 1)),
                Map.of(ItemType.COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE, Map.entry(1200, 1)),
                Map.of(ItemType.JUMBO_BACKPACK_UPGRADE, Map.entry(4000, 1)),
                Map.of(ItemType.MINION_STORAGE_EXPANDER, Map.entry(1500, 1))),
        SACKS("Sacks", new GUIBitsShop(), ItemStackCreator.getStackHead("§5Sacks", "7442c66f4bf9aa4256fa7b49c6367d4658408ec408477879ac8076794402d95b", 1,
                "§7Obtain sack capacity upgrades as well",
                "§7as exclusive bits shop sacks."),
                Map.of(ItemType.POCKET_SACK_IN_A_SACK, Map.entry(8000, 1)),
                Map.of(ItemType.DUNGEON_SACK, Map.entry(14000, 1)),
                Map.of(ItemType.RUNE_SACK, Map.entry(14000, 1)),
                Map.of(ItemType.FLOWER_SACK, Map.entry(14000, 1)),
                Map.of(ItemType.DWARVEN_SACK, Map.entry(14000, 1)),
                Map.of(ItemType.CRYSTAL_HOLLOWS_SACK, Map.entry(14000, 1))),
        DYES("Dyes", new GUIBitsShop(), ItemStackCreator.getStack("§aD§ey§ce§ds", Material.ORANGE_DYE, 1,
                "§7Dyes are exceedingly exclusive items",
                "§7which let you colorize armor pieces."),
                Map.of(ItemType.PURE_WHITE_DYE, Map.entry(250000, 1)),
                Map.of(ItemType.PURE_BLACK_DYE, Map.entry(250000, 1))),
        INFERNO_FUEL_BLOCKS("Inferno Fuel", new GUIBitsShop(), ItemStackCreator.getStackHead("§9Inferno Fuel Blocks", "28a1884ee3f8a6e66692a91ed763cb78d9f2017706d8b42a9263b417b2d715d2", 1,
                "§7Use fuel blocks when creating",
                "§6Inferno §7minion fuel and level up",
                "§7your §cChili Pepper §7collection!"),
                Map.of(ItemType.INFERNO_FUE_BLOCK, Map.entry(75, 1)),
                Map.of(ItemType.INFERNO_FUE_BLOCK, Map.entry(3600, 64))),
        STACKING_ENCHANTS("Stacking Enchants", new GUIBitsShop(), ItemStackCreator.getStack("§9Stacking Enchants", Material.ENCHANTED_BOOK, 1,
                "§7Unlock unique §9enchants §7to apply",
                "§7on your gear.",
                " ",
                "§7Stacking enchants become",
                "§7stronger as you use the gear it's",
                "§7on."),
                Map.of(ItemType.EXPERTISE, Map.entry(4000, 1)),
                Map.of(ItemType.COMPACT, Map.entry(4000, 1)),
                Map.of(ItemType.CULTIVATING, Map.entry(4000, 1)),
                Map.of(ItemType.CHAMPION, Map.entry(4000, 1)),
                Map.of(ItemType.HECATOMB, Map.entry(6000, 1))),
        ENRICHMENTS("Enrichments", new GUIBitsShop(), ItemStackCreator.getStackHead("§dEnrichments", "32fa8f38c7b22096619c3a6d6498b405530e48d5d4f91e2aacea578844d5c67", 1,
                "§7Add a §dboost §7of a stat of your choice",
                "§7to your accessories.",
                " ",
                "§7Only one enrichment may be",
                "§7applied per item."),
                Map.of(ItemType.SPEED_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.INTELLIGENCE_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.CRITICAL_DAMAGE_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.CRITICAL_CHANCE_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.STRENGTH_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.DEFENSE_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.HEALTH_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.MAGIC_FIND_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.FEROCITY_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.SEA_CREATURE_CHANCE_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.ATTACK_SPEED_ENRICHMENT, Map.entry(5000, 1)),
                Map.of(ItemType.ACCESSORY_ENRICHMENT_SWAPPER, Map.entry(200, 1))),
        ;
        private final String guiName;
        private final SkyBlockInventoryGUI previousGUI;
        private final ItemStack.Builder item;
        private final Map<ItemType, Map.Entry<Integer, Integer>>[] itemPrices;

        SubCategorys(String guiName, SkyBlockInventoryGUI previousGUI,ItemStack.Builder item, Map<ItemType, Map.Entry<Integer, Integer>> ...itemPrices) {

            this.guiName = guiName;
            this.previousGUI = previousGUI;
            this.item = item;
            this.itemPrices = itemPrices;
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
        int indexBitItems = 0;
        for (int slot : itemSlots) {
            if (indexBitItems + 1 <= BitItems.values().length) {
                BitItems bitItems = allBitItems[indexBitItems];
                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        if (player.getBits() >= bitItems.price) {
                            SkyBlockItem skyBlockItem = new SkyBlockItem(bitItems.item);
                            ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
                            itemStack.amount(bitItems.amount);
                            SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());
                            if (!player.getPurchaseConfirmationBits()) {
                                player.addAndUpdateItem(finalItem);
                                Integer remainingBits = player.getBits() - bitItems.price;
                                player.setBits(remainingBits);
                                new GUIBitsShop().open(player);
                            } else {
                                new GUIBitsConfirmBuy(finalItem, bitItems.price).open(player);
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
                indexBitItems++;
            }
        }
        SubCategorys[] allSubCategorys = SubCategorys.values();
        int indexSubCategorys = 0;
        for (int slot : categorySlots) {
            SubCategorys subCategorys = allSubCategorys[indexSubCategorys];
            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIBitsSubCategorys(subCategorys.itemPrices, subCategorys.guiName, subCategorys.previousGUI).open(player);
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
            indexSubCategorys++;
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
        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBitsAbiphone().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§5Abiphone Supershop", "785d157db6c9fcc1a5bb24c4590988849933bd355608cae3a6a420660676bc33", 1,
                        "§7Obtain upgrades and special cases",
                        "§7for your Abiphone.",
                        " ",
                        "§7Purchase an Abiphone in the §cCrimson",
                        "§cIsle §7to contact NPCs from afar!");
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
