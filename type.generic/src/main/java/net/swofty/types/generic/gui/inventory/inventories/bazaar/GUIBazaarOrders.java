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
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GUIBazaarOrders extends SkyBlockInventoryGUI {
    // Slots for sell orders (top row)
    private static final int[] SELL_SLOTS = {10,11,12,13,14,15,16};
    // Slots for buy orders (bottom row)
    private static final int[] BUY_SLOTS  = {19,20,21,22,23,24,25};

    public GUIBazaarOrders() {
        super("§8Co-op Bazaar Orders", InventoryType.CHEST_4_ROW);

        // Dark gray background
        fill(ItemStackCreator.createNamedItemStack(Material.GRAY_STAINED_GLASS_PANE));

        // Add section headers
        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return ItemStackCreator.getStack("§6§lSELL ORDERS", Material.GOLD_INGOT, 1,
                        "§7Your active sell orders");
            }
        });

        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer p) {
                return ItemStackCreator.getStack("§a§lBUY ORDERS", Material.EMERALD, 1,
                        "§7Your active buy orders");
            }
        });
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        e.player().getBazaarConnector().getPendingOrders()
                .thenAccept(this::updateOrders);
    }

    private void updateOrders(List<BazaarConnector.BazaarOrder> orders) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        // Clear the sell/buy rows first
        clearSlots();

        // Track next free positions
        int sellIndex = 0, buyIndex = 0;

        for (BazaarConnector.BazaarOrder order : orders) {
            boolean isSell = order.side() == BazaarConnector.OrderSide.SELL;
            int[] slots = isSell ? SELL_SLOTS : BUY_SLOTS;
            int index = isSell ? sellIndex++ : buyIndex++;

            if (index >= slots.length) break; // No more room

            int slot = slots[index];
            ItemType itemType = order.getItemType();
            if (itemType == null) continue; // Skip invalid item types

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                    new GUIBazaarOrderOptions(order).open(p);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    List<String> lore = new ArrayList<>();

                    lore.add("§8Worth " + formatter.format(order.getTotalValue()) + " coins");
                    lore.add(" ");
                    lore.add("§7Order amount: §a" + (int)order.amount() + "§8x");
                    lore.add(" ");
                    lore.add("§7Price per unit: §6" + formatter.format(order.price()) + " coins");
                    lore.add(" ");
                    lore.add("§eClick to view options!");

                    return ItemStackCreator.getStack(
                            (isSell ? "§6§l" : "§a§l") + order.side() + " §f" + itemType.getDisplayName(),
                            itemType.material,
                            1,
                            lore
                    );
                }
            });
        }

        // Add "no orders" placeholders if empty
        if (sellIndex == 0) {
            set(new GUIItem(SELL_SLOTS[0]) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    return ItemStackCreator.getStack("§7No Sell Orders", Material.BARRIER, 1,
                            "§7You don't have any active",
                            "§7sell orders in the Bazaar.");
                }
            });
        }

        if (buyIndex == 0) {
            set(new GUIItem(BUY_SLOTS[0]) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    return ItemStackCreator.getStack("§7No Buy Orders", Material.BARRIER, 1,
                            "§7You don't have any active",
                            "§7buy orders in the Bazaar.");
                }
            });
        }

        // Render changes
        updateItemStacks(getInventory(), getPlayer());
    }

    private void clearSlots() {
        for (int slot : SELL_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    return ItemStack.builder(Material.AIR);
                }
            });
        }
        for (int slot : BUY_SLOTS) {
            set(new GUIItem(slot) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    return ItemStack.builder(Material.AIR);
                }
            });
        }
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