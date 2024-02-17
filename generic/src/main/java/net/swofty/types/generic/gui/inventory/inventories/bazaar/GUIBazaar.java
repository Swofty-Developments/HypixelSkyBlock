package net.swofty.types.generic.gui.inventory.inventories.bazaar;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.commons.ServiceType;
import net.swofty.commons.auctions.AuctionCategories;
import net.swofty.proxyapi.ProxyService;
import net.swofty.types.generic.bazaar.BazaarCategories;
import net.swofty.types.generic.bazaar.BazaarItemSet;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;
import java.util.Map;

public class GUIBazaar extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = new int[]{
            11, 12, 13, 14, 15, 16,
            20, 21, 22, 23, 24, 25,
            29, 30, 31, 32, 33, 34,
            38, 39, 40, 41, 42, 43
    };
    @Getter
    private BazaarCategories category;

    public GUIBazaar(BazaarCategories category) {
        super("Bazaar -> " + StringUtility.toNormalCase(category.name()), InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        int i = 0;
        for (BazaarCategories bazaarCategories : BazaarCategories.values()) {
            set(new GUIClickableItem(i * 9) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

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
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        SkyBlockItem clickedItem = new SkyBlockItem(e.getClickedItem());
        ItemType type = clickedItem.getAttributeHandler().getItemTypeAsType();

        if (clickedItem.isNA()) {
            e.setCancelled(true);
            return;
        }

        if (type == null) {
            e.setCancelled(true);
            return;
        }

        Map.Entry<BazaarCategories, BazaarItemSet> entry = BazaarCategories.getFromItem(type);

        if (entry == null) {
            e.setCancelled(true);
            return;
        }

        new GUIBazaarItem(entry.getKey(), entry.getValue()).open((SkyBlockPlayer) e.getPlayer());
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        if (!new ProxyService(ServiceType.BAZAAR).isOnline().join()) {
            player.sendMessage("§cThe Bazaar is currently offline!");
            player.closeInventory();
        }
    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
