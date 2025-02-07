package net.swofty.types.generic.gui.inventory.actions;

import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class RemoveStateAction implements GUIAction {
    private final String state;

    public RemoveStateAction(String state) {
        this.state = state;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        gui.removeStateInternal(state);
        new RefreshAction().execute(gui);
    }
}