package net.swofty.types.generic.gui.inventory.inventories.auction;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.auctions.*;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemsProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItemLoreHandler;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RefreshAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIAuctionBrowser extends SkyBlockAbstractInventory {
    private static final int[] PAGINATED_SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };

    @Getter
    private List<AuctionItem> itemCache = new ArrayList<>();
    private int page = 1;
    private AuctionsSorting sorting = AuctionsSorting.HIGHEST_BID;
    private AuctionsFilter filter = AuctionsFilter.SHOW_ALL;
    @Getter
    private AuctionCategories category = AuctionCategories.WEAPONS;

    public GUIAuctionBrowser() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Auction Browser")));
        Thread.startVirtualThread(this::updateItemsCache);
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        // Set initial state and title
        doAction(new AddStateAction("category_" + category.name().toLowerCase()));
        doAction(new SetTitleAction(Component.text("Auction Browser - " + StringUtility.toNormalCase(category.name()))));

        // Fill background with category material
        fill(ItemStackCreator.createNamedItemStack(category.getMaterial(), "").build());

        setupBackButton();
        setupSortingButton();
        setupFilterButton();
        setupCategoryButtons();
        setupItemSlots();

        // Start refresh loop
        startLoop("refresh", 10, () -> refreshItems(player));
    }

    private void setupBackButton() {
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Auction House").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionHouse());
                    return true;
                })
                .build());
    }

    private void setupSortingButton() {
        attachItem(GUIItem.builder(50)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(" "));

                    Arrays.stream(AuctionsSorting.values()).forEach(sort -> {
                        if (filter.equals(AuctionsFilter.BIN_ONLY) && sort.equals(AuctionsSorting.MOST_BIDS)) {
                            return;
                        }

                        if (sort == sorting) {
                            lore.add("§b§l> §b" + StringUtility.toNormalCase(sort.name()));
                        } else {
                            lore.add("§7" + StringUtility.toNormalCase(sort.name()));
                        }
                    });

                    lore.add(" ");
                    lore.add("§eClick to switch sort!");

                    return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    AuctionsSorting nextSort = sorting.next();
                    sorting = nextSort;

                    if (filter.equals(AuctionsFilter.BIN_ONLY) && nextSort.equals(AuctionsSorting.MOST_BIDS)) {
                        sorting = AuctionsSorting.HIGHEST_BID;
                    }

                    Thread.startVirtualThread(this::updateItemsCache);
                    return true;
                })
                .build());
    }

    private void setupFilterButton() {
        attachItem(GUIItem.builder(52)
                .item(() -> {
                    List<String> lore = new ArrayList<>(List.of(" "));

                    Arrays.stream(AuctionsFilter.values()).forEach(currentFilter -> {
                        if (currentFilter == filter) {
                            lore.add("§b§l> §b" + StringUtility.toNormalCase(currentFilter.name()));
                        } else {
                            lore.add("§7" + StringUtility.toNormalCase(currentFilter.name()));
                        }
                    });

                    lore.add(" ");
                    lore.add("§bRight-Click to go backwards!");
                    lore.add("§eClick to switch filter!");

                    return ItemStackCreator.getStack("§aBIN Filter", Material.GOLD_BLOCK, 1, lore).build();
                })
                .onClick((ctx, item) -> {
                    filter = ctx.clickType().equals(ClickType.RIGHT_CLICK) ?
                            filter.previous() : filter.next();
                    Thread.startVirtualThread(this::updateItemsCache);
                    return true;
                })
                .build());
    }

    private void setupCategoryButtons() {
        for (int i = 0; i < AuctionCategories.values().length; i++) {
            AuctionCategories currentCategory = AuctionCategories.values()[i];
            int slot = i * 9;

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        List<String> lore = new ArrayList<>(List.of("§8Category", " ", "§7Examples:"));
                        currentCategory.getExamples().forEach(example -> lore.add("§8◼ §7" + example));
                        lore.add(" ");
                        lore.add(currentCategory.equals(category) ? "§aCurrently Browsing" : "§eClick to view!");

                        return ItemStackCreator.getStack(
                                currentCategory.getColor() + StringUtility.toNormalCase(currentCategory.name()),
                                currentCategory.getDisplayMaterial(), 1, lore).build();
                    })
                    .onClick((ctx, item) -> {
                        if (currentCategory.equals(category)) return true;
                        category = currentCategory;
                        Thread.startVirtualThread(this::updateItemsCache);
                        return true;
                    })
                    .build());
        }
    }

    private void setupItemSlots() {
        if (itemCache == null) {
            fillEmptySlots(0);
            return;
        }

        int filledSlots = 0;
        for (AuctionItem auctionItem : itemCache) {
            int slot = PAGINATED_SLOTS[itemCache.indexOf(auctionItem)];
            filledSlots++;

            attachItem(GUIItem.builder(slot)
                    .item(() -> {
                        SkyBlockItem skyBlockItem = new SkyBlockItem(auctionItem.getItem());
                        ItemStack builtItem = PlayerItemUpdater.playerUpdate(owner, skyBlockItem.getItemStack()).build();
                        return ItemStackCreator.getStack(
                                StringUtility.getTextFromComponent(builtItem.get(ItemComponent.CUSTOM_NAME)),
                                skyBlockItem.getMaterial(),
                                skyBlockItem.getAmount(),
                                new AuctionItemLoreHandler(auctionItem).getLore()).build();
                    })
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIAuctionViewItem(auctionItem.getUuid(), this));
                        return true;
                    })
                    .build());
        }

        fillEmptySlots(filledSlots / 2);
    }

    private void fillEmptySlots(int startFrom) {
        for (int i = startFrom; i < PAGINATED_SLOTS.length; i++) {
            int slot = PAGINATED_SLOTS[i];
            attachItem(GUIItem.builder(slot)
                    .item(ItemStack.of(Material.AIR))
                    .build());
        }
    }

    private void updateItemsCache() {
        AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage message =
                new AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage(sorting, filter, category);

        new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message)
                .thenAccept(responseRaw -> {
                    AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse response =
                            (AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse) responseRaw;

                    PaginationList<AuctionItem> paginationList = new PaginationList<>(response.items(), 24);
                    paginationList.addAll(response.items());

                    List<AuctionItem> paginatedItems = paginationList.getPage(page);
                    itemCache = paginatedItems;
                    doAction(new RefreshAction());
                });
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
            return;
        }
        doAction(new RefreshAction());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}