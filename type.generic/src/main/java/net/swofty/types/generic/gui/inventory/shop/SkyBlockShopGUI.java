package net.swofty.types.generic.gui.inventory;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointDouble;
import net.swofty.types.generic.gui.inventory.actions.RefreshAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.shop.GUIGenericTradingOptions;
import net.swofty.types.generic.gui.inventory.shop.ShopItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.components.SellableComponent;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.shop.ShopPrice;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.List;

public abstract class SkyBlockShopGUI extends SkyBlockAbstractInventory {
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

    public SkyBlockShopGUI(String title, int page, int[] guiFormat) {
        super(InventoryType.CHEST_6_ROW);
        this.shopItemList = new ArrayList<>();
        this.page = page;
        this.INTERIOR = guiFormat;
        doAction(new SetTitleAction(Component.text(title)));
        initializeShopItems();
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        for (int slot : INTERIOR) {
            attachItem(GUIItem.builder(slot)
                    .item(ItemStackCreator.createNamedItemStack(Material.AIR).build())
                    .build());
        }

        setupInventoryItems(player);
        setupPaginationButtons();
        setupBuybackButton(player);
        setupShopItems();
    }

    private void setupInventoryItems(SkyBlockPlayer player) {
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = player.getInventory().getItemStack(slot);
            if (stack.material().equals(Material.AIR)) continue;

            SkyBlockItem item = new SkyBlockItem(stack);
            if (item.hasComponent(SellableComponent.class)) {
                updateInventoryItemWithSellPrice(player, slot, stack, item);
            }
        }
    }

    private void updateInventoryItemWithSellPrice(SkyBlockPlayer player, int slot, ItemStack stack, SkyBlockItem item) {
        ItemStack.Builder toReplace = PlayerItemUpdater.playerUpdate(player, stack);
        double sellPrice = item.getComponent(SellableComponent.class).getSellValue() * stack.amount();

        List<String> lore = new ArrayList<>(toReplace.build().get(ItemComponent.LORE)
                .stream()
                .map(StringUtility::getTextFromComponent)
                .toList());

        lore.add("");
        lore.add("§7Sell Price");
        lore.add("§6" + StringUtility.commaify(sellPrice) + " Coin" + (sellPrice != 1 ? "s" : ""));
        lore.add("");
        lore.add("§eClick to sell!");

        toReplace = ItemStackCreator.updateLore(toReplace, lore);
        toReplace.set(ItemComponent.CUSTOM_NAME, Component.text(
                "§a" + StringUtility.getTextFromComponent(toReplace.build().get(ItemComponent.CUSTOM_NAME)) +
                        " §8x" + stack.amount()
        ).decoration(TextDecoration.ITALIC, false));

        player.getInventory().setItemStack(slot, toReplace.build());
    }

    private void setupPaginationButtons() {
        PaginationList<ShopItem> paginatedItems = new PaginationList<>(INTERIOR.length);
        paginatedItems.addAll(shopItemList);

        if (paginatedItems.isEmpty()) page = 0;

        if (page > 1) {
            attachItem(GUIItem.builder(45)
                    .item(ItemStackCreator.createNamedItemStack(Material.ARROW, "§a<-").build())
                    .onClick((ctx, item) -> {
                        page -= 1;
                        ctx.player().openInventory(this);
                        return true;
                    })
                    .build());
        }

        if (page != paginatedItems.getPageCount()) {
            attachItem(GUIItem.builder(53)
                    .item(ItemStackCreator.createNamedItemStack(Material.ARROW, "§a->").build())
                    .onClick((ctx, item) -> {
                        page += 1;
                        ctx.player().openInventory(this);
                        return true;
                    })
                    .build());
        }
    }

    private void setupBuybackButton(SkyBlockPlayer player) {
        attachItem(GUIItem.builder(49)
                .item(() -> {
                    if (!player.getShoppingData().hasAnythingToBuyback()) {
                        return ItemStackCreator.getStack("§aSell Item", Material.HOPPER, 1,
                                "§7Click items in your inventory to",
                                "§7sell them to this Shop!").build();
                    }
                    return createBuybackItemStack(player);
                })
                .onClick((ctx, item) -> {
                    if (!player.getShoppingData().hasAnythingToBuyback())
                        return true;

                    processBuyback(player);
                    return true;
                })
                .build());
    }

    private ItemStack createBuybackItemStack(SkyBlockPlayer player) {
        SkyBlockItem last = new SkyBlockItem(player.getShoppingData().lastBuyback().getKey());
        int amountOfLast = player.getShoppingData().lastBuyback().getValue();
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(player, last.getItemStackBuilder().build());

        double buyBackPrice = last.getComponent(SellableComponent.class).getSellValue() * amountOfLast;
        List<String> lore = new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                .stream()
                .map(StringUtility::getTextFromComponent)
                .toList());

        lore.add("");
        lore.add("§7Cost");
        lore.add("§6" + StringUtility.commaify(buyBackPrice) + " Coin" + (buyBackPrice != 1 ? "s" : ""));
        lore.add("");
        lore.add("§eClick to buyback!");

        itemStack.amount(amountOfLast);
        return ItemStackCreator.updateLore(itemStack, lore).build();
    }

    private void processBuyback(SkyBlockPlayer player) {
        SkyBlockItem last = new SkyBlockItem(player.getShoppingData().lastBuyback().getKey());
        int amountOfLast = player.getShoppingData().lastBuyback().getValue();
        double value = last.getComponent(SellableComponent.class).getSellValue() * amountOfLast;

        double playerCoins = player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue();
        if (playerCoins < value) {
            player.sendMessage("§cYou don't have enough coins!");
            return;
        }

        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(player, last.getItemStackBuilder().build());
        itemStack.amount(amountOfLast);

        player.addAndUpdateItem(new SkyBlockItem(itemStack.build()));
        player.playSuccessSound();
        player.getShoppingData().popBuyback();
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(playerCoins - value);
        player.openInventory(this);
    }

    private void setupShopItems() {
        PaginationList<ShopItem> paginatedItems = new PaginationList<>(INTERIOR.length);
        paginatedItems.addAll(shopItemList);

        List<ShopItem> pageItems = paginatedItems.getPage(page);
        if (pageItems == null) return;

        for (int i = 0; i < pageItems.size(); i++) {
            int slot = INTERIOR[i];
            ShopItem item = pageItems.get(i);
            attachShopItem(slot, item);
        }
    }

    private void attachShopItem(int slot, ShopItem item) {
        attachItem(GUIItem.builder(slot)
                .item(() -> createShopItemStack(item))
                .onClick((ctx, clickedItem) -> {
                    handleShopItemClick(ctx.player(), item, ctx.clickType());
                    return true;
                })
                .build());
    }

    private void attachItem(int slot, ShopItem item) {
        attachShopItem(slot, item);
    }

    private ItemStack createShopItemStack(ShopItem item) {
        ItemStack.Builder itemStack = PlayerItemUpdater.playerUpdate(owner,
                item.getItem().getItemStackBuilder().build());
        itemStack.amount(item.getAmount());

        if (item.getDisplayName() != null) {
            itemStack.set(ItemComponent.CUSTOM_NAME, Component.text(item.getDisplayName())
                    .decoration(TextDecoration.ITALIC, false));
        }

        List<String> lore = item.getLore() != null ? item.getLore() :
                new ArrayList<>(itemStack.build().get(ItemComponent.LORE)
                        .stream()
                        .map(StringUtility::getTextFromComponent)
                        .toList());

        addShopItemLore(lore, item);
        return ItemStackCreator.updateLore(itemStack, lore).build();
    }

    private void addShopItemLore(List<String> lore, ShopItem item) {
        lore.add("");
        lore.add("§7Cost");
        lore.addAll(item.getPrice().getGUIDisplay());
        lore.add("");

        if (item.isHasStock()) {
            lore.add("§7Stock");
            lore.add("§6" + owner.getShoppingData().getStock(item.getItem().toUnderstandable()) + " §7remaining");
            lore.add("");
        }

        lore.add("§eClick to trade!");
        if (item.isStackable()) {
            lore.add("§eRight-click for more trading options!");
        }
    }

    private void handleShopItemClick(SkyBlockPlayer player, ShopItem item, ClickType clickType) {
        if (item.isHasStock() && !player.getShoppingData().canPurchase(
                item.getItem().toUnderstandable(), item.getAmount())) {
            player.sendMessage("§cYou have reached the maximum amount of items you can buy!");
            return;
        }

        if (item.isStackable() && clickType.equals(ClickType.RIGHT_CLICK)) {
            ShopPrice stackPrice = item.getPrice().divide(item.getAmount());
            player.openInventory(new GUIGenericTradingOptions(item, this, stackPrice));
            return;
        }

        if (!item.getPrice().canAfford(player)) {
            player.sendMessage("§cYou don't have enough " + item.getPrice().getNamePlural() + "!");
            return;
        }

        processItemPurchase(player, item);
    }

    private void processItemPurchase(SkyBlockPlayer player, ShopItem item) {
        item.getPrice().processPurchase(player);

        SkyBlockItem purchasedItem = item.getItem();
        purchasedItem.setAmount(item.getAmount());
        player.addAndUpdateItem(purchasedItem);

        player.playSound(Sound.sound(Key.key("block.note_block.pling"),
                Sound.Source.PLAYER, 1.0f, 2.0f));

        if (item.isHasStock()) {
            player.getShoppingData().documentPurchase(
                    item.getItem().toUnderstandable(),
                    item.getAmount());
        }

        player.openInventory(this);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        DataHandler.Data.INVENTORY.onLoad.accept(
                player, DataHandler.Data.INVENTORY.onQuit.apply(player)
        );
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        ItemStack stack = event.getClickedItem();
        event.setCancelled(true);

        if (stack.material().equals(Material.AIR)) return;
        processItemSell((SkyBlockPlayer) event.getPlayer(), stack, event.getSlot());
    }

    private void processItemSell(SkyBlockPlayer player, ItemStack stack, int slot) {
        SkyBlockItem item = new SkyBlockItem(stack);

        if (!item.hasComponent(SellableComponent.class)) {
            player.sendMessage("§cYou can't sell this item!");
            return;
        }

        SellableComponent sellable = item.getComponent(SellableComponent.class);
        double sellPrice = sellable.getSellValue() * stack.amount();

        player.getShoppingData().pushBuyback(item.toUnderstandable(), stack.amount());
        player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).setValue(
                player.getDataHandler().get(DataHandler.Data.COINS, DatapointDouble.class).getValue() + sellPrice
        );

        player.sendMessage(
                "§aYou sold §f" + StringUtility.getTextFromComponent(stack.get(ItemComponent.CUSTOM_NAME)) +
                        "§a for §6" + StringUtility.commaify(sellPrice) + " Coin" +
                        (sellPrice != 1 ? "s" : "") + "§a!"
        );

        player.getInventory().setItemStack(slot, ItemStack.AIR);
        player.openInventory(this);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // Handle any cleanup needed if player suddenly disconnects
    }

    protected abstract void initializeShopItems();

    protected void attachShopItem(ShopItem item) {
        shopItemList.add(item);
    }

    protected void attachItem(ShopItem item) {
        attachShopItem(item);
    }

    protected void updateThis(SkyBlockPlayer player) {
        doAction(new RefreshAction());
    }
}