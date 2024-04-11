package net.swofty.types.generic.gui.inventory;


import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ChestImpl;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIChest extends SkyBlockInventoryGUI {

    private final ChestImpl chest;

    public GUIChest(ChestImpl chest) {
        super("Chest", InventoryType.CHEST_3_ROW);
        this.chest = chest;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        for (int counter = 0; counter < chest.getItems().length; counter++) {
            int finalCounter = counter;
            set(new GUIClickableItem(finalCounter) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    ItemStack stack = e.getCursorItem();
                    chest.setItem(finalCounter, stack);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getFromStack(chest.getItem(finalCounter));
                }

                @Override
                public boolean canPickup() {
                    return true;
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

    }

}
