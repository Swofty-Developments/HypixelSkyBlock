package net.swofty.type.skyblockgeneric.gui.inventories.auction;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponents;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.skyblock.auctions.AuctionCategories;
import net.swofty.commons.skyblock.auctions.AuctionItem;
import net.swofty.commons.skyblock.auctions.AuctionsFilter;
import net.swofty.commons.skyblock.auctions.AuctionsSorting;
import net.swofty.commons.protocol.objects.auctions.AuctionFetchItemsProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.RefreshingGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.utility.PaginationList;
import net.swofty.type.skyblockgeneric.auction.AuctionItemLoreHandler;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
public class GUIAuctionBrowser extends HypixelInventoryGUI implements RefreshingGUI {
    private static final int[] PAGINATED_SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };

    private AuctionsSorting sorting = AuctionsSorting.HIGHEST_BID;
    private AuctionsFilter filter = AuctionsFilter.SHOW_ALL;
    @Getter
    private AuctionCategories category = AuctionCategories.WEAPONS;

    private int page = 1;
    @Getter
    private List<AuctionItem> itemCache = new ArrayList<>();

    public GUIAuctionBrowser() {
        super("Auction Browser", InventoryType.CHEST_6_ROW);

        Thread.startVirtualThread(this::updateItemsCache);
    }

    private void updateItemsCache() {
        AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage message =
                new AuctionFetchItemsProtocolObject.AuctionFetchItemsMessage(
                        sorting,
                        filter,
                        category
                );

        new ProxyService(ServiceType.AUCTION_HOUSE).handleRequest(message)
                .thenAccept(responseRaw -> {
                    AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse response = (AuctionFetchItemsProtocolObject.AuctionFetchItemsResponse) responseRaw;
                    List<AuctionItem> auctionItems = response.items();

                    // Items are already sorted, so just paginate them
                    PaginationList<AuctionItem> paginationList = new PaginationList<>(auctionItems, 24);
                    paginationList.addAll(auctionItems);

                    // Set the items in the GUI
                    List<AuctionItem> paginatedItems = paginationList.getPage(page);
                    setItemCache(paginatedItems);
                });
    }

    @SneakyThrows
    private void setItems() {
        fill(ItemStackCreator.createNamedItemStack(category.getMaterial(), ""));
        set(GUIClickableItem.getGoBackItem(49, new GUIAuctionHouse()));
        getInventory().setTitle(Component.text("Auction Browser - " + StringUtility.toNormalCase(category.name())));

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                AuctionsSorting nextSort = sorting.next();
                setSorting(nextSort);

                if (filter.equals(AuctionsFilter.BIN_ONLY)) {
                    // Ensure that the auctions sorting isn't MOST_BIDS
                    if (nextSort.equals(AuctionsSorting.MOST_BIDS)) {
                        setSorting(AuctionsSorting.HIGHEST_BID);
                    }
                }

                Thread.startVirtualThread(() -> updateItemsCache());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>(List.of(" "));

                Arrays.stream(AuctionsSorting.values()).forEach(sort -> {
                    // Ensure to not display the MOST_BIDS sorting if the filter is BIN_ONLY
                    if (filter.equals(AuctionsFilter.BIN_ONLY)) {
                        if (sort.equals(AuctionsSorting.MOST_BIDS)) {
                            return;
                        }
                    }

                    if (sort == sorting) {
                        lore.add("§b§l> §b" + StringUtility.toNormalCase(sort.name()));
                    } else {
                        lore.add("§7" + StringUtility.toNormalCase(sort.name()));
                    }
                });

                lore.add(" ");
                lore.add("§eClick to switch sort!");

                return ItemStackCreator.getStack("§aSort", Material.HOPPER, 1,
                        lore);
            }
        });
        set(new GUIClickableItem(52) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                if (e.getClick() instanceof Click.Right) {
                    AuctionsFilter nextFilter = filter.previous();
                    setFilter(nextFilter);

                    Thread.startVirtualThread(() -> updateItemsCache());
                    return;
                }
                AuctionsFilter nextFilter = filter.next();
                setFilter(nextFilter);

                Thread.startVirtualThread(() -> updateItemsCache());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                List<String> lore = new ArrayList<>(List.of(" "));

                Arrays.stream(AuctionsFilter.values()).forEach(filter -> {
                    if (filter == GUIAuctionBrowser.this.filter) {
                        lore.add("§b§l> §b" + StringUtility.toNormalCase(filter.name()));
                    } else {
                        lore.add("§7" + StringUtility.toNormalCase(filter.name()));
                    }
                });

                lore.add(" ");
                lore.add("§bRight-Click to go backwards!");
                lore.add("§eClick to switch filter!");

                return ItemStackCreator.getStack("§aBIN Filter", Material.GOLD_BLOCK, 1,
                        lore);
            }
        });
        for (int i = 0; i < AuctionCategories.values().length; i++) {
            AuctionCategories category = AuctionCategories.values()[i];
            set(new GUIClickableItem(i * 9) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (category.equals(getCategory())) {
                        return;
                    }

                    setCategory(category);
                    Thread.startVirtualThread(() -> updateItemsCache());
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    List<String> lore = new ArrayList<>(List.of("§8Category", " ", "§7Examples:"));
                    category.getExamples().forEach(example -> lore.add("§8◼ §7" + example));
                    lore.add(" ");

                    if (category.equals(getCategory())) {
                        lore.add("§aCurrently Browsing");
                    } else {
                        lore.add("§eClick to view items!");
                    }

                    return ItemStackCreator.getStack(category.getColor() + StringUtility.toNormalCase(category.name()), category.getDisplayMaterial(), 1, lore);
                }
            });
        }

        int highestCoveredSlot = 0;

        if (getItemCache() == null) {
            fillInAir(highestCoveredSlot);
            return;
        }

        for (AuctionItem auctionItem : getItemCache()) {
            int slot = PAGINATED_SLOTS[getItemCache().indexOf(auctionItem)];
            highestCoveredSlot++;

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIAuctionViewItem(auctionItem.getUuid(), GUIAuctionBrowser.this).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    SkyBlockItem skyBlockItem = new SkyBlockItem(auctionItem.getItem());
                    ItemStack builtItem = PlayerItemUpdater.playerUpdate(player, skyBlockItem.getItemStack()).build();

                    return ItemStackCreator.getStack(StringUtility.getTextFromComponent(builtItem.get(DataComponents.CUSTOM_NAME)),
                            skyBlockItem.getMaterial(), skyBlockItem.getAmount(), new AuctionItemLoreHandler(auctionItem).getLore());
                }
            });
        }

        // Fill the rest of the slots with air
        fillInAir(highestCoveredSlot / 2);
    }

    private void fillInAir(int highestCoveredSlot) {
        for (int i = highestCoveredSlot; i < PAGINATED_SLOTS.length; i++) {
            int slot = PAGINATED_SLOTS[i];
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStackCreator.getStack(" ", Material.AIR, 1);
                }
            });
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    @Override
    public void refreshItems(HypixelPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }

        setItems();
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}