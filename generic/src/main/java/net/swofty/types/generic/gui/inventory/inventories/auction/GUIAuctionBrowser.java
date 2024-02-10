package net.swofty.types.generic.gui.inventory.inventories.auction;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.commons.auctions.AuctionsFilter;
import net.swofty.commons.auctions.AuctionsSorting;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.auction.AuctionItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemLore;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.PlayerItemUpdater;
import net.swofty.types.generic.protocol.ProtocolFetchItems;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.PaginationList;
import net.swofty.types.generic.utility.StringUtility;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        new ProxyService(ServiceType.AUCTION_HOUSE).callEndpoint(new ProtocolFetchItems(),
                new JSONObject()
                        .put("sorting", sorting)
                        .put("filter", filter)
                        .put("category", category)).thenAccept(response -> {
                            // Get "items" key which is a list of Document JSONs and cast it into a list of Documents
            JSONArray documents = (JSONArray) response.get("items");

            // Convert JSONArray of JSON strings to List<Document>
            List<Document> items = new ArrayList<>();
            for (int i = 0; i < documents.length(); i++) {
                String itemJson = documents.getString(i);
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
        });
    }

    @SneakyThrows
    private void setItems() {
        fill(ItemStackCreator.createNamedItemStack(category.getMaterial(), ""));
        set(GUIClickableItem.getGoBackItem(49, new GUIAuctionHouse()));
        getInventory().setTitle(Component.text("Auction Browser - " + StringUtility.toNormalCase(category.name())));

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
        for (int i = 0; i < AuctionCategories.values().length; i++) {
            AuctionCategories category = AuctionCategories.values()[i];
            set(new GUIClickableItem(i * 9) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    if (category.equals(getCategory())) {
                        return;
                    }

                    setCategory(category);
                    Thread.startVirtualThread(() -> updateItemsCache());
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    List<String> lore = new ArrayList<>(List.of("§8Category", " ", "§7Examples:"));
                    lore.addAll(category.getExamples());
                    lore.add(" ");

                    if (category.equals(getCategory())) {
                        lore.add("§aCurrently Browsing");
                    } else {
                        lore.add("§eClick to view items!");
                    }

                    return ItemStackCreator.getStack(category.getColor() + StringUtility.toNormalCase(category.name()), category.getMaterial(), 1, lore);
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
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    SkyBlockItem skyBlockItem = (auctionItem.getItem());
                    ItemStack builtItem = PlayerItemUpdater.playerUpdate(player, skyBlockItem.getItemStack()).build();

                    return ItemStackCreator.getStack(StringUtility.getTextFromComponent(builtItem.getDisplayName()),
                            skyBlockItem.getMaterial(), skyBlockItem.getAmount(), auctionItem.getLore());
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
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
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

        setItems();
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}