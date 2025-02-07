package net.swofty.types.generic.gui.inventory.actions;

import net.swofty.types.generic.gui.inventory.GUIAction;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;

public class AddStateAction implements GUIAction {
    private final String state;

    public AddStateAction(String state) {
        this.state = state;
    }

    @Override
    public void execute(SkyBlockAbstractInventory gui) {
        gui.addStateInternal(state);
        new RefreshAction().execute(gui);
    }
}