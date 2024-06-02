package net.swofty.types.generic.gui.inventory;

import net.swofty.types.generic.user.SkyBlockPlayer;

public interface RefreshingGUI {
    /**
     * If the GUI implements this method, this is the method that should be using in setting all the items
     * This is called async
     */
    void refreshItems(SkyBlockPlayer player);

    /**
     * How long between each refresh (ticks)
     *
     * @return time in ticks
     */
    int refreshRate();
}