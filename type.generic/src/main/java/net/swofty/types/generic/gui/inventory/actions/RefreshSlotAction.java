package net.swofty.types.generic.gui.inventory.actions;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RefreshSlotAction implements GUIAction {
    private final int slot;

    public RefreshSlotAction(int slot) {
        this.slot = slot;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        GUIItem item = getItem(gui, slot);
        if (item != null) {
            gui.setItemStack(slot, item.getItem());
        } else {
            gui.setItemStack(slot, ItemStack.AIR);
        }
        gui.update();
    }

    public static GUIItem getItem(SkyBlockAbstractInventory gui, int slot) {
        List<GUIItem> items = gui.getItemsInSlot(slot);

        // First try to find an item that requires states and is visible
        Optional<GUIItem> stateItem = Collections.synchronizedList(items).stream()
                .filter(item -> !item.getStateRequirements().isEmpty())
                .filter(item -> item.isVisible(gui.getStates()))
                .findFirst();

        if (stateItem.isPresent()) {
            return stateItem.get();
        }

        // If no state items are visible, get the most recently attached item
        Optional<GUIItem> mostRecentItem = Collections.synchronizedList(items).stream()
                .filter(item -> item.getStateRequirements().isEmpty())
                .max(Comparator.comparingLong(GUIItem::getAttachedTimestamp));

        return mostRecentItem.orElse(null);
    }
}