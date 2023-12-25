package net.swofty.gui.inventory.inventories;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.gui.inventory.ItemStackCreator;
import net.swofty.gui.inventory.RefreshingGUI;
import net.swofty.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.gui.inventory.item.GUIClickableItem;
import net.swofty.item.impl.SkyBlockRecipe;
import net.swofty.user.SkyBlockPlayer;

import java.util.Arrays;

public class GUICrafting extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final ItemStack.Builder RECIPE_REQUIRED = ItemStackCreator.getStack("§cRecipe Required", Material.BARRIER, (short) 0, 1, "§7Add the items for a valid recipe in", "§7the crafting grid to the left!");
    private static final ItemStack.Builder COLLECTION_REQUIRED = ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cCollection Required");
    private static final int[] CRAFT_SLOTS = new int[]{10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int RESULT_SLOT = 24;

    public GUICrafting() {
        super("Craft Item", InventoryType.CHEST_6_ROW);
    }


    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
        border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE));
        border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);
        set(GUIClickableItem.getCloseItem(49));

        set(RESULT_SLOT, RECIPE_REQUIRED);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            e.getPlayer().getInventory().addItemStack(e.getInventory().getItemStack(slot));
        });
    }

    @Override
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {
        Arrays.stream(CRAFT_SLOTS).forEach(slot -> {
            player.getInventory().addItemStack(inventory.getItemStack(slot));
        });
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {}

    @Override
    public void refreshItems(SkyBlockPlayer player) {
        Inventory inventory = getInventory();
        ItemStack[] recipe = getCurrentRecipe(inventory);

        SkyBlockRecipe<?> skyBlockRecipe = SkyBlockRecipe.parseRecipe(getCurrentRecipe(inventory));

        if (skyBlockRecipe == null) {
            fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 13, 34);
            border(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE));
            border(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE), 0, 44);
            set(GUIClickableItem.getCloseItem(49));

            set(RESULT_SLOT, RECIPE_REQUIRED);
            return;
        }

        // TODO: Recipe checking
    }

    @Override
    public int refreshRate() {
        return 5;
    }

    private ItemStack[] getCurrentRecipe(Inventory inventory) {
        ItemStack[] stacks = new ItemStack[9];
        for (int i = 0; i < CRAFT_SLOTS.length; i++)
            stacks[i] = inventory.getItemStack(CRAFT_SLOTS[i]);
        return stacks;
    }
}
