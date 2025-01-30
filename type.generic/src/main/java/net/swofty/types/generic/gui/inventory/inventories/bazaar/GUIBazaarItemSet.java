package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GUIBazaarItemSet extends SkyBlockAbstractInventory {
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
        super(InventoryType.CHEST_4_ROW);
        this.category = category;
        this.itemSet = itemSet;

        doAction(new SetTitleAction(Component.text(
                StringUtility.toNormalCase(category.name()) + " -> " + itemSet.displayName)));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Back button
        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To " + new GUIBazaar(category).getTitle()).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaar(category));
                    return true;
                })
                .build());

        // Manage Orders button
        attachItem(GUIItem.builder(32)
                .item(ItemStackCreator.getStack("§aManage Orders", Material.BOOK, 1,
                        "§7You don't have any ongoing orders.",
                        " ",
                        "§eClick to manage!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaarOrders());
                    return true;
                })
                .build());

        setupBazaarItems(player);
        startLoop("refresh", 10, () -> refreshItems(player));
    }

    private void setupBazaarItems(SkyBlockPlayer player) {
        List<CompletableFuture> futures = new ArrayList<>();

        int i = 0;
        for (ItemType itemType : itemSet.items) {
            CompletableFuture future = new CompletableFuture();
            futures.add(future);
            int slot = SLOTS.get(itemSet.items.size())[i];

            Thread.startVirtualThread(() -> {
                BazaarGetItemProtocolObject.BazaarGetItemMessage message =
                        new BazaarGetItemProtocolObject.BazaarGetItemMessage(itemType.name());
                CompletableFuture<BazaarGetItemProtocolObject.BazaarGetItemResponse> futureBazaar =
                        new ProxyService(ServiceType.BAZAAR).handleRequest(message);
                BazaarItem item = futureBazaar.join().item();

                attachItem(GUIItem.builder(slot)
                        .item(() -> {
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
                                    itemType.rarity.getColor() + itemType.getDisplayName(),
                                    itemType.material, 1, lore).build();
                        })
                        .onClick((ctx, clickedItem) -> {
                            ctx.player().openInventory(new GUIBazaarItem(itemType));
                            return true;
                        })
                        .build());

                future.complete(null);
            });
            i++;
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline().join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
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

        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getPotentialType();

        if (clickedItem.isNA() || type == null) {
            return;
        }

        Map.Entry<BazaarCategories, BazaarItemSet> entry = BazaarCategories.getFromItem(type);
        if (entry == null) {
            return;
        }

        Thread.startVirtualThread(() -> {
            event.getPlayer().openInventory(new GUIBazaarItemSet(entry.getKey(), entry.getValue()));
        });
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {}
}