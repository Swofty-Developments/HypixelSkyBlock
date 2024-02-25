package net.swofty.types.generic.gui.inventory.inventories.bazaar;

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
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.protocol.ProtocolPingSpecification;
import net.swofty.types.generic.protocol.bazaar.ProtocolBazaarGetItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIBazaarItem extends SkyBlockInventoryGUI implements RefreshingGUI {
    private final ItemType itemType;
    public GUIBazaarItem(ItemType itemType) {
        super(BazaarCategories.getFromItem(itemType).getKey() + " -> " + itemType.getDisplayName(),
                InventoryType.CHEST_4_ROW);

        this.itemType = itemType;

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(30, new GUIBazaarItemSet(
                BazaarCategories.getFromItem(itemType).getKey(),
                BazaarCategories.getFromItem(itemType).getValue())));
        set(new GUIClickableItem(31) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIBazaar(BazaarCategories.getFromItem(itemType).getKey()).open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStackHead("§6Go Back",
                        "c232e3820897429157619b0ee099fec0628f602fff12b695de54aef11d923ad7",
                        1, "§7To Bazaar");
            }
        });
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
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return new NonPlayerItemUpdater(new SkyBlockItem(itemType)).getUpdatedItem();
            }
        });
    }


    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        Thread.startVirtualThread(() -> {
            BazaarItem item = (BazaarItem) new ProxyService(ServiceType.BAZAAR).callEndpoint(
                    new ProtocolBazaarGetItem(),
                    new JSONObject().put("item-name", itemType.name()).toMap()
            ).join().get("item");

            updateItems(item);
        });
    }

    public void updateItems(BazaarItem item) {
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + itemType.getDisplayName());
                lore.add(" ");

                if (!item.getSellOrders().isEmpty()) {
                    double pricePerUnit = item.getSellStatistics().getLowestOrder();
                    lore.add("§7Price per unit: §6" + pricePerUnit + " coins");
                    lore.add("§7Stack price: §6" + (pricePerUnit * 64) + " coins");
                    lore.add(" ");
                    lore.add("§eClick to pick amount!");
                } else {
                    lore.add("§cNo buy orders available.");
                    lore.add(" ");
                    lore.add("§8Cannot buy instantly");
                }

                return ItemStackCreator.getStack("§aBuy Instantly", Material.GOLDEN_HORSE_ARMOR,
                        1, lore);
            }
        });
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>();
                lore.add("§8" + itemType.getDisplayName());
                lore.add(" ");

                int amountInInventory = player.getAllOfTypeInInventory(itemType).size();

                if (amountInInventory == 0) {
                    lore.add("§7Inventory: §cNone!");
                    lore.add(" ");
                    lore.add("§7Price Per Unit: §6" + item.getBuyStatistics().getHighestOrder() + " coins");
                    lore.add(" ");
                    lore.add("§8None to sell in your inventory!");
                } else {
                    lore.add("§7Inventory: §a" + amountInInventory + " items");
                    lore.add(" ");

                    if (!item.getBuyOrders().isEmpty()) {
                        double pricePerUnit = item.getBuyStatistics().getHighestOrder();
                        lore.add("§7Amount: §a" + amountInInventory + "§7x");
                        lore.add("§7Total price: §6" + (pricePerUnit * amountInInventory) + " coins");
                        lore.add(" ");
                        lore.add("§eClick to sell!");
                    } else {
                        lore.add("§cNo sell orders available.");
                        lore.add(" ");
                        lore.add("§8Cannot sell instantly");
                    }
                }

                return ItemStackCreator.getStack("§6Sell Instantly", Material.HOPPER,
                        1, lore);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
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

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
