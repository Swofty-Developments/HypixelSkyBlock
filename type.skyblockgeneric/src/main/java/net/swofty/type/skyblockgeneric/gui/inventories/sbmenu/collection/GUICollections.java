package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.collection;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.collection.CollectionCategories;
import net.swofty.type.skyblockgeneric.collection.CollectionCategory;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUICollections extends HypixelInventoryGUI {
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
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
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

        set(new GUIClickableItem(50) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                new GUICraftedMinions(new GUICollections()).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStackHead("§aCrafted Minions", "ebcc099f3a00ece0e5c4b31d31c828e52b06348d0a4eac11f3fcbef3c05cb407", 1,
                "§7View all the unique minions that you",
                        "§7have crafted.",
                        "",
                        "§eClick to view!");
            }
        });

        int index = 0;
        for (int slot : displaySlots) {
            CollectionCategory category = allCategories.get(index);

            ArrayList<String> display = new ArrayList<>();
            ((SkyBlockPlayer) getPlayer()).getCollection().getDisplay(display, category);

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUICollectionCategory(category, display).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
