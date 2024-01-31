package net.swofty.types.generic.gui.inventory.inventories.auction;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.AuctionCategories;
import net.swofty.commons.AuctionsFilter;
import net.swofty.commons.AuctionsSorting;
import net.swofty.commons.ServiceType;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemLore;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Setter
public class GUIAuctionBrowser extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] PAGINATED_SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };

    private AuctionsSorting sorting = AuctionsSorting.HIGHEST_BID;
    private AuctionsFilter filter = AuctionsFilter.SHOW_ALL;
    private AuctionCategories category = AuctionCategories.WEAPONS;

    private int page = 1;
    @Getter
    private List<AuctionItem> itemCache = new ArrayList<>();
    private Lock itemCacheLock = new ReentrantLock();

    public GUIAuctionBrowser() {
        super("Auction Browser", InventoryType.CHEST_6_ROW);

        fill(ItemStackCreator.createNamedItemStack(category.getMaterial(), ""));
        set(GUIClickableItem.getGoBackItem(49, new GUIAuctionHouse()));

        Thread.startVirtualThread(this::updateItemsCache);
    }

    synchronized void updateItemsCache() {
        itemCacheLock.lock();

        JSONObject documents = new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint("fetch-items", new JSONObject()
                .put("sorting", sorting.name())
                .put("filter", filter.name())
                .put("category", category.name())).join();

        // Get the items as a JSONArray
        JSONArray itemsJson = documents.getJSONArray("items");

        // Convert JSONArray of JSON strings to List<Document>
        List<Document> items = new ArrayList<>();
        for (int i = 0; i < itemsJson.length(); i++) {
            String itemJson = itemsJson.getString(i);
            Document itemDocument = Document.parse(itemJson);
            items.add(itemDocument);
        }

        List<AuctionItem> auctionItems = items.stream().map(AuctionItem::fromDocument).toList();

        // Items are already sorted, so just paginate them
        PaginationList<AuctionItem> paginationList = new PaginationList<>(auctionItems, 24);
        paginationList.addAll(auctionItems);

        // Set the items in the GUI
        List<AuctionItem> paginatedItems = paginationList.getPage(page);
        setItemCache(paginatedItems);

        itemCacheLock.unlock();
    }

    @SneakyThrows
    synchronized void setItems() {
        boolean isLockAcquired = itemCacheLock.tryLock(300, TimeUnit.MILLISECONDS);

        if (!isLockAcquired) {
            return;
        }

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                AuctionsSorting nextSort = sorting.next();
                setSorting(nextSort);

                if (filter.equals(AuctionsFilter.BIN_ONLY)) {
                    // Ensure that the auctions sorting isn't MOST_BIDS
                    if (nextSort.equals(AuctionsSorting.MOST_BIDS)) {
                        setSorting(AuctionsSorting.HIGHEST_BID);
                    }
                }

                Thread.startVirtualThread(() -> updateItemsCache());
                Thread.startVirtualThread(() -> setItems());
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
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

        int highestCoveredSlot = 0;
        for (AuctionItem auctionItem : getItemCache()) {
            int slot = PAGINATED_SLOTS[getItemCache().indexOf(auctionItem)];
            highestCoveredSlot++;

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    SkyBlockItem skyBlockItem = (auctionItem.getItem());
                    ItemLore itemLore = new ItemLore(skyBlockItem.getItemStack());

                    List<String> lore = new ArrayList<>(itemLore.getStack().getLore().stream().map(StringUtility::getTextFromComponent)
                            .toList());

                    return ItemStackCreator.getStack("Example", skyBlockItem.getMaterial(), skyBlockItem.getAmount(), lore);
                }
            });
        }

        // Fill the rest of the slots with air
        for (int i = highestCoveredSlot; i < PAGINATED_SLOTS.length; i++) {
            int slot = PAGINATED_SLOTS[i];
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack(" ", Material.AIR, 1);
                }
            });
        }

        Logger.info("Updated items");
        updateItemStacks(getInventory(), getPlayer());
        itemCacheLock.unlock();
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
        e.setCancelled(true);
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.AUCTION_HOUSE).isOnline().join()) {
            player.sendMessage("§cAuction House is currently offline!");
            player.closeInventory();
        }

        Thread.startVirtualThread(this::setItems);
    }

    @Override
    public int refreshRate() {
        return 20;
    }
}