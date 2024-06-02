package net.swofty.types.generic.gui.inventory.inventories.sbmenu.collection;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CollectionCategories;
import net.swofty.types.generic.collection.CollectionCategory;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollections extends SkyBlockInventoryGUI {
    private final int[] displaySlots = {
            20, 21, 22, 23, 24,
                    31
    };

    public GUICollections() {
        super("Collections", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        ArrayList<CollectionCategory> allCategories = CollectionCategories.getCategories();

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                List<String> lore = new ArrayList<>(List.of(
                        "§7View all of the items available in",
                        "§7SkyBlock. Collect more of an item to",
                        "§7unlock rewards on your way to",
                        "§7becoming a master of SkyBlock!",
                        " "
                ));

                player.getCollection().getDisplay(lore);

                lore.add(" ");
                lore.add("§eClick to view!");
                return ItemStackCreator.getStack("§aCollections", Material.PAINTING, 1, lore.toArray(new String[0]));
            }
        });

        int index = 0;
        for (int slot : displaySlots) {
            CollectionCategory category = allCategories.get(index);

            ArrayList<String> display = new ArrayList<>();
            getPlayer().getCollection().getDisplay(display, category);

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUICollectionCategory(category, display).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§7View your " + category.getName() + " Collections!",
                            " "
                    ));

                    lore.addAll(display);

                    return ItemStackCreator.getStack(
                            "§a" + category.getName() + " Collections", category.getDisplayIcon(),
                            1, lore);
                }
            });

            index++;
        }
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {

    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
