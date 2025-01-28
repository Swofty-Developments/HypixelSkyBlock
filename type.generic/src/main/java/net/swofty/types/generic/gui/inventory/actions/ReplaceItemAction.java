package net.swofty.types.generic.gui.inventory.actions;

import net.minestom.server.item.ItemStack;
import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class ReplaceItemAction implements GUIAction {
    private final String id;
    private final ItemStack newItem;

    public ReplaceItemAction(String id, ItemStack newItem) {
        this.id = id;
        this.newItem = newItem;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        gui.replaceItemWithId(id, newItem);
    }
}