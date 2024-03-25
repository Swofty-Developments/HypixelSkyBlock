package net.swofty.types.generic.gui.inventory.inventories.sbmenu.bags;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.collection.CustomCollectionAward;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.GUISkyBlockMenu;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIYourBags extends SkyBlockInventoryGUI {
    public GUIYourBags() {
        super("Your Bags", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, new GUISkyBlockMenu()));

        SkyBlockPlayer player = e.player();

        if (player.hasCustomCollectionAward(CustomCollectionAward.QUIVER)) {
            set(new GUIClickableItem(23) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIQuiver().open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead("§aQuiver",
                            "396ce13ff6155fdf3235d8d22174c5de4bf5512f1adeda1afa3fc28180f3f7", 1,
                            "§7A masterfully crafted Quiver which",
                            "§7holds any kind of projectile you can",
                            "§7think of!",
                            " ",
                            "§eClick to open!");
                }
            });
        } else {
            set(new GUIItem(23) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§cQuiver", Material.GRAY_DYE, 1,
                            "§7A masterfully crafted Quiver which",
                            "§7holds any kind of projectile you can",
                            "§7think of!",
                            " ",
                            "§cRequires §aString Collection III§c.");
                }
            });
        }

        updateItemStacks(e.inventory(), e.player());
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
