package net.swofty.type.generic.gui.inventory.item;

import com.mongodb.lang.Nullable;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;

public abstract class GUIQueryItem extends GUIClickableItem {
    public GUIQueryItem(int slot) {
        super(slot);
    }

    /**
     * Runs when the player clicks this item
     * @param e the event of the click
     */
    @Override
    public void run(InventoryPreClickEvent e, HypixelPlayer player) {}

    /**
     * Run when the player enters something into the sign gui
     * @param query the string the player enters
     * @return the gui that should be opened back after this
     */
    public abstract HypixelInventoryGUI onQueryFinish(@Nullable String query, HypixelPlayer player);

    /**
     * The preset lines on the sign GUI
     * @return an array that needs only 2 components
     */
    public String[] lines() {
        return new String[]{"Enter your", "input"};
    }
}