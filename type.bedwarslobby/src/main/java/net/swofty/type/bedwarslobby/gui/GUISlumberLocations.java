package net.swofty.type.bedwarslobby.gui;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUISlumberLocations extends HypixelInventoryGUI {

    public GUISlumberLocations() {
        super("Slumber Locations", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIClickableItem(11) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.teleport(new Pos(17, 69, 0, -90, 0));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aTeleport to Doorman Dave",
                        Material.OAK_DOOR,
                        1
                );
            }
        });
        set(new GUIClickableItem(13) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.teleport(new Pos(43, 69, 0, -90, 0));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§eTeleport to Reception",
                        Material.RED_BED,
                        1
                );
            }
        });
        set(new GUIClickableItem(15) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.teleport(new Pos(34.5, 69.5, 15.5, 90, 0));
                player.closeInventory();
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§6Teleport to the Ticket Machine",
                        Material.NAME_TAG,
                        1
                );
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
