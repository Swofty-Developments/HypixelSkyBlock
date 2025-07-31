package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.bazaar.BazaarConnector;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUIBazaarOrderOptions extends SkyBlockInventoryGUI {
    private final BazaarConnector.BazaarOrder order;

    public GUIBazaarOrderOptions(BazaarConnector.BazaarOrder order) {
        super("Order Options", InventoryType.CHEST_3_ROW);
        this.order = order;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(18, new GUIBazaarOrders()));
        set(GUIClickableItem.getCloseItem(26));

        setupItems();
    }

    private void setupItems() {
        ItemType itemType = order.getItemType();
        if (itemType == null) return;

        DecimalFormat formatter = new DecimalFormat("#,###.##");
        boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;

        // Order details display
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + (isSell ? "Sell" : "Buy") + " Order");
                lore.add(" ");
                lore.add("§7Amount: §a" + (int)order.amount() + "§8x");
                lore.add("§7Price per unit: §6" + formatter.format(order.price()) + " coins");
                lore.add("§7Total value: §6" + formatter.format(order.getTotalValue()) + " coins");
                lore.add(" ");
                lore.add("§7Order ID: §8" + order.orderId().toString().substring(0, 8) + "...");

                return ItemStackCreator.getStack(
                        (isSell ? "§6" : "§a") + itemType.getDisplayName(),
                        itemType.material,
                        1,
                        lore
                );
            }
        });

        // Cancel order button
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                p.sendMessage("§6[Bazaar] §7Cancelling order...");

                p.getBazaarConnector().cancelOrder(order.orderId())
                        .thenAccept(success -> {
                            if (success) {
                                p.sendMessage("§6[Bazaar] §aOrder cancelled successfully!");
                                if (order.side() == BazaarConnector.OrderSide.BUY) {
                                    p.setCoins(p.getCoins() + order.price() * order.amount());
                                } else {
                                    SkyBlockItem item = new SkyBlockItem(order.getItemType());
                                    item.setAmount((int) order.amount());
                                    p.addAndUpdateItem(item);
                                }

                                new GUIBazaarOrders().open(p);
                            } else {
                                p.sendMessage("§6[Bazaar] §cFailed to cancel order. It may have already been filled or expired.");
                            }
                        });
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return ItemStackCreator.getStack("§cCancel Order", Material.BARRIER, 1,
                        "§7Cancel this bazaar order and",
                        "§7return your items/coins.",
                        " ",
                        "§cThis action cannot be undone!",
                        " ",
                        "§eClick to cancel!");
            }
        });

        // View market button
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                new GUIBazaarItem(itemType).open(p);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return ItemStackCreator.getStack("§aView Market", Material.EMERALD, 1,
                        "§7View the current market for",
                        "§7" + itemType.getDisplayName(),
                        " ",
                        "§eClick to view!");
            }
        });
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}