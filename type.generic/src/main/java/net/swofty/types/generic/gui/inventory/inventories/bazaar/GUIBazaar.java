package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.StringUtility;
import net.swofty.commons.bazaar.BazaarItem;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetItemProtocolObject;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.RemoveStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Getter
public class GUIBazaar extends SkyBlockAbstractInventory {
    private static final String STATE_ITEMS_LOADED = "items_loaded";
    private static final String STATE_LOADING = "loading";

    private static final int[] SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };

    private final BazaarCategories category;

    public GUIBazaar(BazaarCategories category) {
        super(InventoryType.CHEST_6_ROW);
        this.category = category;

        doAction(new SetTitleAction(Component.text("Bazaar -> " +
                StringUtility.toNormalCase(category.name()))));

        startLoop("refresh", 10, () -> refreshItems(owner));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        doAction(new AddStateAction(STATE_LOADING));

        fill(ItemStackCreator.createNamedItemStack(category.getGlassItem()).build());
        setupCategoryButtons();
        setupNavigationButtons();
        loadBazaarItems();
    }

    private void setupCategoryButtons() {
        int i = 0;
        for (BazaarCategories bazaarCategories : BazaarCategories.values()) {
            final int index = i;
            attachItem(GUIItem.builder(index * 9)
                    .item(() -> {
                        ItemStack.Builder builder = ItemStackCreator.getStack(
                                bazaarCategories.getColor() + StringUtility.toNormalCase(bazaarCategories.name()),
                                bazaarCategories.getDisplayItem(), 1,
                                "§8Category", " ",
                                (category == bazaarCategories ? "§aCurrently Viewing" : "§eClick to view!")
                        );

                        if (category == bazaarCategories) {
                            builder = ItemStackCreator.enchant(builder);
                        }

                        return builder.build();
                    })
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIBazaar(bazaarCategories));
                        return true;
                    })
                    .build());
            i++;
        }
    }

    private void setupNavigationButtons() {
        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Manage orders button
        attachItem(GUIItem.builder(50)
                .item(ItemStackCreator.getStack("§aManage Orders", Material.BOOK, 1,
                        "§7You don't have any ongoing orders.",
                        " ",
                        "§eClick to manage!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaarOrders());
                    return true;
                })
                .build());
    }

    private void loadBazaarItems() {
        Thread.startVirtualThread(() -> {
            int index = 0;
            for (int slot : SLOTS) {
                if (category.getItems().size() <= index) {
                    attachItem(GUIItem.builder(slot)
                            .item(ItemStack.AIR)
                            .build());
                    continue;
                }

                BazaarItemSet itemSet = (BazaarItemSet) category.getItems().toArray()[index];
                index++;

                setupItemSetButton(slot, itemSet);
            }

            doAction(new RemoveStateAction(STATE_LOADING));
            doAction(new AddStateAction(STATE_ITEMS_LOADED));
        });
    }

    private void setupItemSetButton(int slot, BazaarItemSet itemSet) {
        List<String> lore = new ArrayList<>(Arrays.asList(
                "§8" + itemSet.items.size() + " products", " "
        ));

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ItemType type : itemSet.items) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            futures.add(future);

            ProxyService baseService = new ProxyService(ServiceType.BAZAAR);
            BazaarGetItemProtocolObject.BazaarGetItemMessage message =
                    new BazaarGetItemProtocolObject.BazaarGetItemMessage(type.name());
            CompletableFuture<BazaarGetItemProtocolObject.BazaarGetItemResponse> futureBazaar =
                    baseService.handleRequest(message);

            Thread.startVirtualThread(() -> {
                BazaarItem bazaarItem = futureBazaar.join().item();

                lore.add(type.rarity.getColor() + "▶ §7" + type.getDisplayName()
                        + " §c" + StringUtility.shortenNumber(bazaarItem.getSellStatistics().getMeanOrder()) +
                        " §8| §a" + StringUtility.shortenNumber(bazaarItem.getBuyStatistics().getMeanOrder()));
                future.complete(null);
            });
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        lore.add(" ");
        lore.add("§eClick to view products!");

        attachItem(GUIItem.builder(slot)
                .item(ItemStackCreator.getStack(category.getColor() + itemSet.displayName,
                        itemSet.displayMaterial.material, 1, lore).build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIBazaarItemSet(category, itemSet));
                    return true;
                })
                .requireState(STATE_ITEMS_LOADED)
                .build());
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
    public void onBottomClick(InventoryPreClickEvent event) {
        SkyBlockItem clickedItem = new SkyBlockItem(event.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getPotentialType();
        event.setCancelled(true);

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
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}