package net.swofty.gui.inventory.item;

import com.mongodb.lang.Nullable;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.swofty.gui.SkyBlockAnvilGUI;
import net.swofty.gui.SkyBlockSignGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.user.SkyBlockPlayer;

public interface GUIQueryItem extends GUIClickableItem {
    /**
     * Run when the player clicks this item
     *
     * @param e the event of the click
     */
    void run(InventoryPreClickEvent e, SkyBlockPlayer player);

    /**
     * Run when the player enters something into the sign gui
     *
     * @param query the string the player enters
     * @return the gui that should be opened back after this
     */
    SkyBlockInventoryGUI onQueryFinish(@Nullable String query, SkyBlockPlayer player);

    /**
     * The preset lines on the sign GUI
     *
     * @return an array that needs only 2 components
     */
    default String[] lines() {
        return new String[]{"Enter your", "input"};
    }
}