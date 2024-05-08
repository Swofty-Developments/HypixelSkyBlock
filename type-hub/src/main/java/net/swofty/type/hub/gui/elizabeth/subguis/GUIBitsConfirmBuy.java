package net.swofty.type.hub.gui.elizabeth.subguis;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.hub.gui.elizabeth.GUIBitsShop;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

public class GUIBitsConfirmBuy extends SkyBlockInventoryGUI {

    SkyBlockItem item;
    Integer price;
    public GUIBitsConfirmBuy(SkyBlockItem item, Integer price) {
        super("Confirm", InventoryType.CHEST_3_ROW);
        this.item = item;
        this.price = price;
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(new GUIClickableItem(11) {

            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.addAndUpdateItem(item);
                Integer remainingBits = player.getBits() - price;
                player.setBits(remainingBits);
                new GUIBitsShop().open(player);
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aConfirm", Material.LIME_TERRACOTTA, 1,
                        "§7Buying: " + item.getDisplayName(),
                        "§7Cost: §b" + StringUtility.commaify(price));
            }
        });
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§cCancel", Material.RED_TERRACOTTA, 1);
            }
        });
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
    }
}
