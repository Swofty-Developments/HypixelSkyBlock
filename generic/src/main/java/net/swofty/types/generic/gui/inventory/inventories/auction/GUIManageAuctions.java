package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIManageAuctions extends SkyBlockInventoryGUI implements RefreshingGUI {
    public GUIManageAuctions() {
        super("Manage Auctions", InventoryType.CHEST_3_ROW);

        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS));
        set(GUIClickableItem.getGoBackItem(22, new GUIAuctionHouse()));
        set(new GUIClickableItem(23) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                new GUIAuctionCreateItem().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCreate Auction", Material.GOLDEN_HORSE_ARMOR, 1,
                        "§7Set your own items on auction for",
                        "§7other players to purchase.",
                        " ",
                        "§eClick to become rich!");
            }
        });
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

    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {

    }

    @Override
    public int refreshRate() {
        return 10;
    }
}
