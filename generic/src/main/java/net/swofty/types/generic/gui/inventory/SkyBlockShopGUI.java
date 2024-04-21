package net.swofty.types.generic.gui.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.gui.inventory.inventories.shop.GUIGenericTradingOptions;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.impl.Sellable;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyBlockShopGUI extends SkyBlockInventoryGUI {

    public static final int[] DEFAULT = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };
    public static final int[] WOOLWEAVER_VIBRANT = new int[]{
            1,  2,  3,  4,  5,  6,  7,  8,
            10, 11, 12, 13, 14, 15, 16, 17,
            19, 20, 21, 22, 23, 24, 25, 26,
            28, 29, 30, 31, 32, 33, 34, 35,
            37, 38, 39, 40, 41, 42, 43, 44
    };
    public static final int[] WOOLWEAVER_COOL = new int[]{
            0,  1,  2,  3,  4,  5,  6,  7,
            9,  10, 11, 12, 13, 14, 15, 16,
            18, 19, 20, 21, 22, 23, 24, 25,
            17, 28, 29, 30, 31, 32, 33, 34,
            26, 37, 38, 39, 40, 41, 42, 43,
    };
    public static final int[] UPPER5ROWS = new int[]{
            0,  1,  2,  3,  4,  5,  6,  7,  8,
            9,  10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    public static final int[] GREENTHUMB = new int[]{
            9,  10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
                    38, 39, 40, 41, 42
    };
    public static final int[] VARIETY = new int[]{
            0,  1,  2,  3,  4,  5,  6,  7,  8,
            9,  10, 11, 12, 13, 14, 15, 16, 17,
            18, 19, 20, 21, 22, 23, 24, 25, 26,
            27, 28, 29, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42
    };

    private final List<ShopItem> shopItemList;
    private int page;
    private int[] INTERIOR;

    public SkyBlockShopGUI(String title, int page, int[]guiFormat) {
        super(title, InventoryType.CHEST_6_ROW);
        this.shopItemList = new ArrayList<>();
        this.page = page;
        this.INTERIOR = guiFormat;
        initializeShopItems();
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();

        DataHandler.Data.INVENTORY.onLoad.accept(
                player, DataHandler.Data.INVENTORY.onQuit.apply(player)
        );
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " "));
        for (int slot : INTERIOR) {
            set(slot, ItemStackCreator.createNamedItemStack(Material.AIR));
        }
        PaginationList<ShopItem> paginatedItems = new PaginationList<>(INTERIOR.length);
        paginatedItems.addAll(shopItemList);

        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = getPlayer().getInventory().getItemStack(slot);

            if (stack.material().equals(Material.AIR)) continue;

            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.getGenericInstance() instanceof Sellable sellable) {
                ItemStack.Builder toReplace = PlayerItemUpdater.playerUpdate(
                        getPlayer(), stack
                );

                double sellPrice = sellable.getSellValue() * stack.amount();
                List<String> lore = new ArrayList<>(toReplace.build().getLore()
                        .stream()
                        .map(StringUtility::getTextFromComponent)
                        .toList());

                lore.add("");
                lore.add("§7Sell Price");
                lore.add("§6" + StringUtility.commaify(sellPrice) + " Coin" + (sellPrice != 1 ? "s" : ""));
                lore.add("");
                lore.add("§eClick to sell!");

                toReplace.lore(lore.stream().map(
                        line -> Component.text(line).decoration(TextDecoration.ITALIC, false)
                ).toList());
                toReplace.displayName(Component.text(
                        "§a" + StringUtility.getTextFromComponent(toReplace.build().getDisplayName()) +
                                " §8x" + stack.amount()
                ).decoration(TextDecoration.ITALIC, false));

                getPlayer().getInventory().setItemStack(slot, toReplace.build());
            }
        }

        if (paginatedItems.isEmpty()) page = 0;
        if (page > 1)
            set(new GUIClickableItem(45) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    SkyBlockShopGUI.this.page -= 1;
                    SkyBlockShopGUI.this.open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.createNamedItemStack(Material.ARROW, "§a<-");
                }
            });

        if (page != paginatedItems.getPageCount())
            set(new GUIClickableItem(53) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    SkyBlockShopGUI.this.page += 1;
                    SkyBlockShopGUI.this.open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.createNamedItemStack(Material.ARROW, "§a->");
                }
            });

        /*
            Buyback item
        */
        set(new GUIClickableItem(49) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                if (!player.getShoppingData().hasAnythingToBuyback())
                    return;

                SkyBlockItem last = player.getShoppingData().lastBuyback().getKey();
                int amountOfLast = player.getShoppingData().lastBuyback().getValue();
                ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                        player, last.getItemStackBuilder().build()
                );
                itemStack.amount(amountOfLast);

                double value = (last.getGenericInstance() instanceof Sellable ? ((Sellable) last.getGenericInstance()).getSellValue() : 1)
                        * amountOfLast;

                double playerCoins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
                if (playerCoins < value) {
                    player.sendMessage("§cYou don't have enough coins!");
                    return;
                }

                player.addAndUpdateItem(new SkyBlockItem(itemStack.build()));
                player.playSuccessSound();
                player.getShoppingData().popBuyback();
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(playerCoins - value);
                updateThis(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                if (!player.getShoppingData().hasAnythingToBuyback()) {
                    return ItemStackCreator.getStack("§aSell Item", Material.HOPPER, (short) 0, 1,
                            "§7Click items in your inventory to",
                            "§7sell them to this Shop!");
                }

                SkyBlockItem last = player.getShoppingData().lastBuyback().getKey();
                int amountOfLast = player.getShoppingData().lastBuyback().getValue();
                ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                        player, last.getItemStackBuilder().build()
                );

                double buyBackPrice = ((Sellable) last.getGenericInstance()).getSellValue() * amountOfLast;

                List<String> lore = new ArrayList<>(itemStack.build().getLore()
                        .stream()
                        .map(StringUtility::getTextFromComponent)
                        .toList());
                lore.add("");
                lore.add("§7Cost");
                lore.add("§6" + StringUtility.commaify(buyBackPrice) + " Coin" + (buyBackPrice != 1 ? "s" : ""));
                lore.add("");
                lore.add("§eClick to buyback!");

                itemStack.amount(amountOfLast);
                return itemStack.lore(lore.stream().map(
                        line -> Component.text(line).decoration(TextDecoration.ITALIC, false)
                ).toList());
            }
        });

        List<ShopItem> p = paginatedItems.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++) {
            int slot = INTERIOR[i];
            ShopItem item = p.get(i);
            SkyBlockItem sbItem = item.item;

            ShopPrice price = item.price;
            ShopPrice stackPrice = item.price.divide(item.amount);

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (item.isHasStock() && !player.getShoppingData().canPurchase(item.item, item.amount)) {
                        player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
                        return;
                    }

                    if (item.isStackable() && e.getClickType().equals(ClickType.RIGHT_CLICK)) {
                        new GUIGenericTradingOptions(item, SkyBlockShopGUI.this, stackPrice).open(player);
                        return;
                    }

                    if (!price.canAfford(player)) {
                        player.sendMessage("§cYou don't have enough " + price.getNamePlural() + "!");
                        return;
                    }

                    price.processPurchase(player);

                    sbItem.setAmount(item.amount);
                    player.addAndUpdateItem(sbItem);
                    player.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 2.0f));

                    if (item.hasStock)
                        player.getShoppingData().documentPurchase(item.getItem(), item.amount);

                    updateThis(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(
                            player, sbItem.getItemStackBuilder().build()
                    );

                    if (item.getDisplayName() != null)
                        itemStack.displayName(Component.text(item.getDisplayName())
                                .decoration(TextDecoration.ITALIC, false));

                    List<String> lore;

                    if (item.getLore() != null) {
                        lore = item.lore;
                    } else {
                        lore = new ArrayList<>(itemStack.build().getLore()
                                .stream()
                                .map(StringUtility::getTextFromComponent)
                                .toList());
                    }

                    lore.add("");
                    lore.add("§7Cost");
                    lore.addAll(price.getGUIDisplay());
                    lore.add("");
                    if (item.hasStock) {
                        lore.add("§7Stock");
                        lore.add("§6" + getPlayer().getShoppingData().getStock(item.getItem()) + " §7remaining");
                        lore.add("");
                    }
                    lore.add("§eClick to trade!");

                    if (item.stackable)
                        lore.add("§eRight-click for more trading options!");

                    return itemStack.lore(lore.stream().map(
                            line -> Component.text(line).decoration(TextDecoration.ITALIC, false)
                    ).toList()).amount(item.amount);
                }
            });
        }
        updateItemStacks(e.inventory(), getPlayer());
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        ItemStack stack = e.getClickedItem();
        e.setCancelled(true);
        if (stack.material().equals(Material.AIR)) return;
        SkyBlockItem item = new SkyBlockItem(stack);

        Sellable sellable;
        if (item.getGenericInstance() instanceof Sellable sellableInstance) {
            sellable = sellableInstance;
        } else {
            e.getPlayer().sendMessage("§cYou can't sell this item!");
            return;
        }

        double sellPrice = sellable.getSellValue() * stack.amount();

        getPlayer().getShoppingData().pushBuyback(item, stack.getAmount());
        getPlayer().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                getPlayer().getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + sellPrice
        );
        getPlayer().sendMessage(
                "§aYou sold §f" + StringUtility.getTextFromComponent(stack.getDisplayName()) + "§a for §6"
                        + StringUtility.commaify(sellPrice) + " Coin" + (sellPrice != 1 ? "s" : "") + "§a!"
        );

        getPlayer().playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1f, 2f), Sound.Emitter.self());

        getPlayer().getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
        updateThis(getPlayer());
    }

    public abstract void initializeShopItems();

    public void attachItem(ShopItem i) {
        shopItemList.add(i);
    }

    public void updateThis(SkyBlockPlayer player) {
        SkyBlockShopGUI.this.open(player);
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ShopItem {
        private final SkyBlockItem item;
        private final int amount;
        private final ShopPrice price;
        private final boolean stackable;
        private List<String> lore = null;
        @Setter
        private String displayName = null;
        private boolean hasStock = true;

        public ShopItem(SkyBlockItem item, int amount, ShopPrice price, boolean stackable, boolean hasStock) {
            this.item = item;
            this.amount = amount;
            this.price = price;
            this.stackable = stackable;
            this.hasStock = hasStock;
        }
        
        public void setDisplayLore(List<String> lores) {
            this.lore = lores;
        }

        public static ShopItem Stackable(SkyBlockItem item, int amount, ShopPrice price) {
            return new ShopItem(item, amount, price, true);
        }

        public static ShopItem Single(SkyBlockItem item, int amount, ShopPrice price) {
            return new ShopItem(item, amount, price, false);
        }

    }
}
