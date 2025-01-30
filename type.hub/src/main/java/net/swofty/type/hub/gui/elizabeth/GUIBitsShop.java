package net.swofty.type.hub.gui.elizabeth;

import lombok.Getter;
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
import net.swofty.type.hub.gui.elizabeth.subguis.GUIBitsAbiphone;
import net.swofty.type.hub.gui.elizabeth.subguis.GUIBitsConfirmBuy;
import net.swofty.type.hub.gui.elizabeth.subguis.GUIBitsSubCategories;
import net.swofty.types.generic.data.datapoints.DatapointToggles;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GUIBitsShop extends SkyBlockAbstractInventory {
    private static final String STATE_TAB_PREFIX = "tab_";
    private static final String STATE_CATEGORY_PREFIX = "category_";
    private static final String STATE_CAN_AFFORD_PREFIX = "can_afford_";

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
    @Getter
    private enum SubCategorys {
        KAT_ITEMS("Kat Items", new GUIBitsShop(), ItemStackCreator.enchant(ItemStackCreator.getStack("§bKat Items", Material.RED_TULIP, 1,
                "§7Reduce the amount of time it takes",
                "§7to upgrade your pet at §bKat§7.")),
                List.of(
                        new CommunityShopItem(ItemType.KAT_FLOWER, 500, 1),
                        new CommunityShopItem(ItemType.KAT_BOUQUET, 2500, 1)
                )),
        UPGRADE_COMPONENTS("Upgrade Components", new GUIBitsShop(), ItemStackCreator.getStackHead("§cUpgrade Components", "59358703ab7727df3324336969e81d6f92b7aa79edb966c0be91ab161bad1f01", 1,
                "§7Upgrade many items in SkyBlock",
                "§7through special crafting",
                "§7components."),
                List.of(
                        new CommunityShopItem(ItemType.HEAT_CORE, 3000, 1),
                        new CommunityShopItem(ItemType.HYPER_CATALYST_UPGRADER, 300, 1),
                        new CommunityShopItem(ItemType.ULTIMATE_CARROT_CANDY_UPGRADE, 8000, 1),
                        new CommunityShopItem(ItemType.COLOSSAL_EXPERIENCE_BOTTLE_UPGRADE, 1200, 1),
                        new CommunityShopItem(ItemType.JUMBO_BACKPACK_UPGRADE, 4000, 1),
                        new CommunityShopItem(ItemType.MINION_STORAGE_EXPANDER, 1500, 1)
                )),
        SACKS("Sacks", new GUIBitsShop(), ItemStackCreator.getStackHead("§5Sacks", "7442c66f4bf9aa4256fa7b49c6367d4658408ec408477879ac8076794402d95b", 1,
                "§7Obtain sack capacity upgrades as well",
                "§7as exclusive bits shop sacks."),
                List.of(
                        new CommunityShopItem(ItemType.POCKET_SACK_IN_A_SACK, 8000, 1),
                        new CommunityShopItem(ItemType.DUNGEON_SACK, 14000, 1),
                        new CommunityShopItem(ItemType.RUNE_SACK, 14000, 1),
                        new CommunityShopItem(ItemType.FLOWER_SACK, 14000, 1),
                        new CommunityShopItem(ItemType.DWARVEN_SACK, 14000, 1),
                        new CommunityShopItem(ItemType.CRYSTAL_HOLLOWS_SACK, 14000, 1)
                )),
        DYES("Dyes", new GUIBitsShop(), ItemStackCreator.getStack("§aD§ey§ce§ds", Material.ORANGE_DYE, 1,
                "§7Dyes are exceedingly exclusive items",
                "§7which let you colorize armor pieces."),
                List.of(
                        new CommunityShopItem(ItemType.PURE_WHITE_DYE, 250000, 1),
                        new CommunityShopItem(ItemType.PURE_BLACK_DYE, 250000, 1)
                )),
        INFERNO_FUEL_BLOCKS("Inferno Fuel", new GUIBitsShop(), ItemStackCreator.getStackHead("§9Inferno Fuel Blocks", "28a1884ee3f8a6e66692a91ed763cb78d9f2017706d8b42a9263b417b2d715d2", 1,
                "§7Use fuel blocks when creating",
                "§6Inferno §7minion fuel and level up",
                "§7your §cChili Pepper §7collection!"),
                List.of(
                        new CommunityShopItem(ItemType.INFERNO_FUEL_BLOCK, 75, 1),
                        new CommunityShopItem(ItemType.INFERNO_FUEL_BLOCK, 3600, 64)
                )),
        STACKING_ENCHANTS("Stacking Enchants", new GUIBitsShop(), ItemStackCreator.getStack("§9Stacking Enchants", Material.ENCHANTED_BOOK, 1,
                "§7Unlock unique §9enchants §7to apply",
                "§7on your gear.",
                " ",
                "§7Stacking enchants become",
                "§7stronger as you use the gear it's",
                "§7on."),
                List.of(
                        new CommunityShopItem(ItemType.EXPERTISE, 4000, 1),
                        new CommunityShopItem(ItemType.COMPACT, 4000, 1),
                        new CommunityShopItem(ItemType.CULTIVATING, 4000, 1),
                        new CommunityShopItem(ItemType.CHAMPION, 4000, 1),
                        new CommunityShopItem(ItemType.HECATOMB, 6000, 1)
                )),
        ENRICHMENTS("Enrichments", new GUIBitsShop(), ItemStackCreator.getStackHead("§dEnrichments", "32fa8f38c7b22096619c3a6d6498b405530e48d5d4f91e2aacea578844d5c67", 1,
                "§7Add a §dboost §7of a stat of your choice",
                "§7to your accessories.",
                " ",
                "§7Only one enrichment may be",
                "§7applied per item."),
                List.of(
                        new CommunityShopItem(ItemType.SPEED_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.INTELLIGENCE_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.CRITICAL_DAMAGE_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.CRITICAL_CHANCE_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.STRENGTH_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.DEFENSE_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.HEALTH_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.MAGIC_FIND_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.FEROCITY_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.SEA_CREATURE_CHANCE_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.ATTACK_SPEED_ENRICHMENT, 5000, 1),
                        new CommunityShopItem(ItemType.ACCESSORY_ENRICHMENT_SWAPPER, 200, 1)
                )),
        ;

        private final String guiName;
        private final SkyBlockAbstractInventory previousGUI;
        private final ItemStack.Builder item;
        private final List<CommunityShopItem> shopItems;

        SubCategorys(String guiName, SkyBlockAbstractInventory previousGUI, ItemStack.Builder item, List<CommunityShopItem> shopItems) {
            this.guiName = guiName;
            this.previousGUI = previousGUI;
            this.item = item;
            this.shopItems = shopItems;
        }
    }

    public GUIBitsShop() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Community Shop")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build())
                .build());

        doAction(new AddStateAction(STATE_TAB_PREFIX + "4")); // Set initial tab state
        doAction(new AddStateAction(STATE_CATEGORY_PREFIX + "13")); // Set initial category state

        setupTabs();
        setupCategories();
        setupBitItems(player);
        setupSubCategories();
        setupControlButtons();
        setupAbiphoneButton();
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
                        if (slot != 4) {
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
                            slot != 13 ? Material.GRAY_STAINED_GLASS_PANE : Material.GREEN_STAINED_GLASS_PANE,
                            1, "§8▼ §7Items").build())
                    .build());
        }
    }

    private void setupBitItems(SkyBlockPlayer player) {
        BitItems[] allBitItems = BitItems.values();

        for (int i = 0; i < Math.min(itemSlots.length, allBitItems.length); i++) {
            final int slot = itemSlots[i];
            final BitItems bitItem = allBitItems[i];

            String affordState = STATE_CAN_AFFORD_PREFIX + slot;
            if (player.getBits() >= bitItem.price) {
                doAction(new AddStateAction(affordState));
            }

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        SkyBlockItem item = new SkyBlockItem(bitItem.item);
                        ItemStack.Builder itemStack = new NonPlayerItemUpdater(item).getUpdatedItem();
                        ArrayList<String> lore = new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                                .stream().map(StringUtility::getTextFromComponent).toList());
                        lore.add(" ");
                        lore.add("§7Cost");
                        lore.add("§b" + StringUtility.commaify(bitItem.price) + " Bits");
                        lore.add(" ");
                        lore.add("§eClick to trade!");
                        return ItemStackCreator.updateLore(itemStack, lore).build();
                    })
                    .onClick((ctx, item) -> handleBitItemPurchase(ctx.player(), bitItem))
                    .build());
        }
    }

    private boolean handleBitItemPurchase(SkyBlockPlayer player, BitItems bitItem) {
        if (player.getBits() >= bitItem.price) {
            SkyBlockItem skyBlockItem = new SkyBlockItem(bitItem.item);
            ItemStack.Builder itemStack = new NonPlayerItemUpdater(skyBlockItem).getUpdatedItem();
            itemStack.amount(bitItem.amount);
            SkyBlockItem finalItem = new SkyBlockItem(itemStack.build());

            if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS)) {
                player.addAndUpdateItem(finalItem);
                player.setBits(player.getBits() - bitItem.price);
                player.openInventory(new GUIBitsShop());
            } else {
                player.openInventory(new GUIBitsConfirmBuy(finalItem, bitItem.price));
            }
            return true;
        }

        player.sendMessage("§cYou don't have enough Bits to buy that!");
        return false;
    }

    private void setupSubCategories() {
        SubCategorys[] allSubCategorys = SubCategorys.values();

        for (int i = 0; i < Math.min(categorySlots.length, allSubCategorys.length); i++) {
            final int slot = categorySlots[i];
            final SubCategorys category = allSubCategorys[i];

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        ItemStack.Builder itemstack = category.item;
                        ArrayList<String> lore = new ArrayList<>(itemstack.build().get(ItemComponent.LORE)
                                .stream().map(StringUtility::getTextFromComponent).toList());
                        if (!Objects.equals(lore.getLast(), "§eClick to browse!")) {
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
    }

    private void setupControlButtons() {
        // Community Shop Button
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
                    openStoreBook(ctx.player());
                    return true;
                })
                .build());

        // Purchase Confirmation Toggle
        attachItem(GUIItem.builder(48)
                .item(() -> {
                    String status = owner.getToggles().get(DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS) ?
                            "§aEnabled!" : "§cOFF";
                    return ItemStackCreator.getStack("§aPurchase Confirmation",
                            Material.COMPARATOR, 1,
                            "§7Buying a lot and never",
                            "§7second-guess a decision?",
                            " ",
                            "§7Confirmations: " + status,
                            " ",
                            "§eClick to toggle confirm menu!").build();
                })
                .onClick((ctx, item) -> {
                    ctx.player().getToggles().set(DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS, false);
                    ctx.player().openInventory(new GUIBitsShop());
                    return true;
                })
                .build());
    }

    private void setupAbiphoneButton() {
        attachItem(GUIItem.builder(33)
                .item(ItemStackCreator.getStackHead("§5Abiphone Supershop",
                        "785d157db6c9fcc1a5bb24c4590988849933bd355608cae3a6a420660676bc33", 1,
                        "§7Obtain upgrades and special cases",
                        "§7for your Abiphone.",
                        " ",
                        "§7Purchase an Abiphone in the §cCrimson",
                        "§cIsle §7to contact NPCs from afar!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBitsAbiphone());
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
