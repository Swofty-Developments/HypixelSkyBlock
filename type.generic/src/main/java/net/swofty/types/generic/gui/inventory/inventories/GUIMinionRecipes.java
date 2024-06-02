package net.swofty.types.generic.gui.inventory.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe.GUIRecipe;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIMinionRecipes extends SkyBlockInventoryGUI {
    private static final int[] RECIPE_SLOTS = new int[]{
            11, 12, 13, 14, 15,
               21, 22, 23,
               30, 31, 32
    };

    SkyBlockInventoryGUI previousGUI;
    MinionRegistry registry;

    public GUIMinionRecipes(MinionRegistry registry, SkyBlockInventoryGUI previousGUI) {
        super(registry.getDisplay() + " Recipes", InventoryType.CHEST_6_ROW);

        this.registry = registry;
        this.previousGUI = previousGUI;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        if (previousGUI != null)
            set(GUIClickableItem.getGoBackItem(48, previousGUI));

        AtomicInteger i = new AtomicInteger();
        Arrays.stream(RECIPE_SLOTS).forEach(slot -> {
            i.getAndIncrement();

            SkyBlockItem item = new SkyBlockItem(registry.getItemType());
            item.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(
                    i.get(),
                    0
            ));

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(item, GUIMinionRecipes.this, i.get() - 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return new NonPlayerItemUpdater(item).getUpdatedItem();
                }
            });
        });
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
