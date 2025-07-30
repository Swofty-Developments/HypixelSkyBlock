package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.ServiceType;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocolObject;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage;
import net.swofty.commons.protocol.objects.bazaar.BazaarGetPendingOrdersProtocolObject.PendingOrder;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
        ProxyService bazaar = new ProxyService(ServiceType.BAZAAR);

        bazaar.<BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
                        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse>handleRequest(
                        new BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage(
                                e.player().getUuid()
                        )
                )
                .thenAccept(resp -> {
                    updateOrders(resp.orders);
                });
    }

    private void updateOrders(List<PendingOrder> orders) {
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        // Clear the sell/buy rows first
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

        // Track next free positions
        int sellIndex = 0, buyIndex = 0;

        for (PendingOrder order : orders) {
            boolean isSell = "SELL".equalsIgnoreCase(order.side());
            int[] slots = isSell ? SELL_SLOTS : BUY_SLOTS;
            int index = isSell ? sellIndex++ : buyIndex++;

            if (index >= slots.length) break; // No more room

            int slot = slots[index];
            ItemType itemType;

            try {
                itemType = ItemType.valueOf(order.itemName());
            } catch (IllegalArgumentException e) {
                continue; // Skip invalid item types
            }

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer p) {
                    new GUIBazaarOrderOptions(order).open(p);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer p) {
                    List<String> lore = new ArrayList<>();

                    if (isSell) {
                        lore.add("§8Worth " + formatter.format(order.price() * order.amount()) + " coins");
                        lore.add(" ");
                        lore.add("§7Order amount: §a" + (int)order.amount() + "§8x");
                        lore.add(" ");
                        lore.add("§7Price per unit: §6" + formatter.format(order.price()) + " coins");
                    } else {
                        lore.add("§8Worth " + formatter.format(order.price() * order.amount()) + " coins");
                        lore.add(" ");
                        lore.add("§7Order amount: §a" + (int)order.amount() + "§8x");
                        lore.add(" ");
                        lore.add("§7Price per unit: §6" + formatter.format(order.price()) + " coins");
                    }

                    lore.add(" ");
                    lore.add("§eClick to view options!");

                    return ItemStackCreator.getStack(
                            (isSell ? "§6§l" : "§a§l") + (isSell ? "SELL" : "BUY") + " §f" + itemType.getDisplayName(),
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

    @Override public boolean allowHotkeying() { return false; }
    @Override public void onBottomClick(InventoryPreClickEvent e) { e.setCancelled(true); }
}