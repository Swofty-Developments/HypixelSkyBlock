package net.swofty.types.generic.gui.inventory.actions;

import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class RefreshAction implements GUIAction {
    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        gui.getInventoryType().getSize();
        for (int i = 0; i < gui.getInventoryType().getSize(); i++) {
            new RefreshSlotAction(i).execute(gui);
        }
    }
}