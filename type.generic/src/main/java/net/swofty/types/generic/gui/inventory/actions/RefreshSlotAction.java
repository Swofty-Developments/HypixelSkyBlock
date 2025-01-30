package net.swofty.types.generic.gui.inventory.actions;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

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
        List<GUIItem> items = gui.getItemsInSlot(slot);

        // First try to find an item that requires states and is visible
        Optional<GUIItem> stateItem = items.stream()
                .filter(item -> !item.getStateRequirements().isEmpty())
                .filter(item -> item.isVisible(gui.getStates()))
                .findFirst();

        if (stateItem.isPresent()) {
            gui.setItemStack(slot, stateItem.get().getItem());
            return;
        }

        // If no state items are visible, get the most recently attached item
        Optional<GUIItem> mostRecentItem = items.stream()
                .filter(item -> item.getStateRequirements().isEmpty())
                .max(Comparator.comparingLong(GUIItem::getAttachedTimestamp));

        gui.setItemStack(slot, mostRecentItem.map(GUIItem::getItem).orElse(ItemStack.AIR));
    }
}