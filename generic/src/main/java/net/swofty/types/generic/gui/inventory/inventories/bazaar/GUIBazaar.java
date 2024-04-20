package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
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
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.bazaar.ProtocolBazaarGetItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class GUIBazaar extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };
    private final BazaarCategories category;

    public GUIBazaar(BazaarCategories category) {
        super("Bazaar -> " + StringUtility.toNormalCase(category.name()), InventoryType.CHEST_6_ROW);

        this.category = category;

        fill(ItemStackCreator.createNamedItemStack(category.getGlassItem()));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem(50) {
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
        int i = 0;
        for (BazaarCategories bazaarCategories : BazaarCategories.values()) {
            set(new GUIClickableItem(i * 9) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIBazaar(bazaarCategories).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ItemStack.Builder builder = ItemStackCreator.getStack(
                            bazaarCategories.getColor() + StringUtility.toNormalCase(bazaarCategories.name()),
                            bazaarCategories.getDisplayItem(), 1,
                            "§8Category", " ",
                            (category == bazaarCategories ? "§aCurrently Viewing" : "§eClick to view!")
                    );

                    if (category == bazaarCategories) {
                        builder = ItemStackCreator.enchant(builder);
                    }

                    return builder;
                }
            });

            i++;
        }

        updateItemStacks(getInventory(), getPlayer());

        Thread.startVirtualThread(() -> {
            int i1 = 0;
            for (int j : SLOTS) {
                if (category.getItems().size() <= i1) {
                    set(new GUIItem(j) {
                        @Override
                        public ItemStack.Builder getItem(SkyBlockPlayer player) {
                            return ItemStack.builder(Material.AIR);
                        }
                    });
                    continue;
                }
                BazaarItemSet itemSet = (BazaarItemSet) category.getItems().toArray()[i1];
                i1++;

                List<String> lore = new ArrayList<>(Arrays.asList(
                        "§8" + itemSet.items.size() + " products", " "
                ));

                // Save a list of futures for every item in the set
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (ItemType type : itemSet.items) {
                    // Create a new future for each item
                    CompletableFuture<Void> future = new CompletableFuture<>();
                    futures.add(future);

                    // Call the bazaar-get-item endpoint
                    Map<String, Object> values = new JSONObject().put("item-name", type.name()).toMap();

                    ProxyService baseService = new ProxyService(ServiceType.BAZAAR);
                    baseService.callEndpoint(new ProtocolBazaarGetItem(), values).thenAccept(response -> {
                        BazaarItem bazaarItem = (BazaarItem) response.get("item");

                        lore.add(type.rarity.getColor() + "▶ §7" + type.getDisplayName(null)
                                + " §c" + StringUtility.shortenNumber(bazaarItem.getSellStatistics().getMeanOrder()) +
                                " §8| §a" + StringUtility.shortenNumber(bazaarItem.getBuyStatistics().getMeanOrder()));
                        future.complete(null);
                    });
                }

                // Wait for all futures to complete by joining them
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                lore.add(" ");
                lore.add("§eClick to view products!");

                set(new GUIClickableItem(j) {
                    @Override
                    public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                        new GUIBazaarItemSet(category, itemSet).open(player);
                    }

                    @Override
                    public ItemStack.Builder getItem(SkyBlockPlayer player) {
                        return ItemStackCreator.getStack(category.getColor() + itemSet.displayName,
                                itemSet.displayMaterial.material, 1, lore);
                    }
                });
            }

            updateItemStacks(getInventory(), getPlayer());
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
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
