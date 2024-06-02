package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.bazaar.ProtocolBazaarGetItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GUIBazaarItemSet extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final Map<Integer, int[]> SLOTS = Map.of(
            1, new int[]{13},
            2, new int[]{12, 14},
            3, new int[]{11, 13, 15},
            4, new int[]{10, 12, 14, 16},
            5, new int[]{11, 12, 13, 14, 15},
            6, new int[]{11, 12, 13, 14, 15, 22},
            7, new int[]{10, 11, 12, 13, 14, 15, 16},
            8, new int[]{11, 12, 13, 14, 15, 21, 22, 23},
            9, new int[]{10, 11, 12, 13, 14, 15, 16, 21, 23},
            10, new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24}
    );

    private final BazaarCategories category;
    private final BazaarItemSet itemSet;

    public GUIBazaarItemSet(BazaarCategories category, BazaarItemSet itemSet) {
        super(StringUtility.toNormalCase(category.name()) + " -> " + itemSet.displayName, InventoryType.CHEST_4_ROW);

        this.category = category;
        this.itemSet = itemSet;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaar(category)));
        set(new GUIClickableItem(32) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBazaarOrders().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aManage Orders", Material.BOOK, 1,
                        "§7You don't have any ongoing orders.",
                        " ",
                        "§eClick to manage!");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        List<CompletableFuture> futures = new ArrayList<>();

        int i = 0;
        for (ItemType itemType : itemSet.items) {
            CompletableFuture future = new CompletableFuture();
            futures.add(future);
            int slot = SLOTS.get(itemSet.items.size())[i];

            Thread.startVirtualThread(() -> {
                BazaarItem item = (BazaarItem) new ProxyService(ServiceType.BAZAAR)
                        .callEndpoint(new ProtocolBazaarGetItem(),
                                new JSONObject().put("item-name", itemType.name()).toMap())
                        .join()
                        .get("item");

                set(new GUIClickableItem(slot) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUIBazaarItem(itemType).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        List<String> lore = new ArrayList<>();
                        lore.add("§8" + StringUtility.toNormalCase(itemType.rarity.name()) + " commodity");
                        lore.add(" ");

                        lore.add("§7Buy price: §6" +
                                new DecimalFormat("#,###").format(item.getSellStatistics().getLowestOrder())
                                + " coins");
                        lore.add("§8" + StringUtility.shortenNumber(item.getSellStatistics().getHighestOrder())
                                + " in " + item.getSellOrders().size() + " offers");

                        lore.add(" ");
                        lore.add("§7Sell price: §6" +
                                new DecimalFormat("#,###").format(item.getBuyStatistics().getHighestOrder())
                                + " coins");
                        lore.add("§8" + StringUtility.shortenNumber(item.getBuyStatistics().getLowestOrder())
                                + " in " + item.getBuyOrders().size() + " offers");

                        lore.add(" ");
                        lore.add("§eClick to view details!");

                        return ItemStackCreator.getStack(
                                itemType.rarity.getColor() + itemType.getDisplayName(null),
                                itemType.material, 1, lore);
                    }
                });

                future.complete(null);
            });
            i++;
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

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
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getItemTypeAsType();
        e.setCancelled(true);

        if (clickedItem.isNA()) {
            return;
        }

        if (type == null) {
            return;
        }

        Map.Entry<BazaarCategories, BazaarItemSet> entry = BazaarCategories.getFromItem(type);

        if (entry == null) {
            return;
        }

        Thread.startVirtualThread(() -> {
            new GUIBazaarItemSet(entry.getKey(), entry.getValue()).open((SkyBlockPlayer) e.getPlayer());
        });
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline(new ProtocolPingSpecification()).join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
