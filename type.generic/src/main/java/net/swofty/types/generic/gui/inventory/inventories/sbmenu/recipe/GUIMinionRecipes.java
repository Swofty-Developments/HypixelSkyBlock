package net.swofty.types.generic.gui.inventory.inventories.sbmenu.recipe;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.StringUtility;
import net.swofty.commons.item.attribute.attributes.ItemAttributeMinionData;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.minion.MinionRegistry;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIMinionRecipes extends SkyBlockInventoryGUI {
    private static final Map<Integer, int[]> SLOTS = new HashMap<>(Map.of(
            10, new int[] { 11, 12, 13, 14, 15, 20, 21, 22, 23, 24 },
            11, new int[] { 11, 12, 13, 14, 15, 21, 22, 23, 30, 31, 32 }
    ));

    SkyBlockInventoryGUI previousGUI;
    MinionRegistry minionRegistry;

    public GUIMinionRecipes(MinionRegistry minionRegistry, SkyBlockInventoryGUI previousGUI) {
        super(StringUtility.toNormalCase(minionRegistry.toString()) + " Minion Recipes", InventoryType.CHEST_6_ROW);
        this.previousGUI = previousGUI;
        this.minionRegistry = minionRegistry;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.getGoBackItem(48, previousGUI));

        List<SkyBlockMinion.MinionTier> craftableMinionTiers = new ArrayList<>();

        for (SkyBlockMinion.MinionTier minionTier : minionRegistry.asSkyBlockMinion().getTiers()) {
            if (minionTier.craftable()) craftableMinionTiers.add(minionTier);
        }

        int[] slots = SLOTS.get(craftableMinionTiers.size());
        int i = 0;
        for (SkyBlockMinion.MinionTier minionTier : craftableMinionTiers) {
            int slot = slots[i];
            i++;
            SkyBlockItem minion = new SkyBlockItem(minionRegistry.getItemType());
            minion.getAttributeHandler().setMinionData(new ItemAttributeMinionData.MinionData(minionTier.tier(), 0));

            set(new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIRecipe(minion, new GUIMinionRecipes(minionRegistry, previousGUI), minionTier.tier() - 1).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStackHead(minion.getDisplayName(), minionTier.texture(), 1,
                            minion.getLore());
                }
            });
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
