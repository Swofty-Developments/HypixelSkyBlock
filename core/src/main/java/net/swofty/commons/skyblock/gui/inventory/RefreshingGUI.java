package net.swofty.commons.skyblock.gui.inventory;

import net.swofty.commons.skyblock.user.SkyBlockPlayer;

public interface RefreshingGUI {
    /**
     * If the GUI implements this method, this is the method that should be using in setting all the items
     */
    void refreshItems(SkyBlockPlayer player);

    /**
     * How long between each refresh (ticks)
     *
     * @return time in ticks
     */
    int refreshRate();
}