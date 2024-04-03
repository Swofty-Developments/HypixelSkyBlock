package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIEmblems extends SkyBlockInventoryGUI {
    private static int[] SLOTS = new int[]{
            11, 12, 13, 14, 15
    };

    public GUIEmblems() {
        super("Emblems", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(31));
        set(GUIClickableItem.getGoBackItem(30, new GUISkyBlockLevels()));

        Arrays.stream(SkyBlockEmblems.values()).forEach(emblem -> {
            set(new GUIClickableItem(SLOTS[emblem.ordinal()]) {
                @Override
                public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                    new GUIEmblem(emblem).open(player);
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    String displayName = emblem.toString();
                    Material material = emblem.getDisplayMaterial();
                    ArrayList<String> description = new ArrayList<>(emblem.getDescription());

                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§8" + emblem.amountUnlocked(player) + " Unlocked",
                            " "
                    ));
                    lore.addAll(description);
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getStack("§a" + displayName, material, 1, lore);
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
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
