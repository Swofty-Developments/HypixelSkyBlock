package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIMaterial;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.type.skyblockgeneric.levels.SkyBlockEmblems;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIEmblems extends HypixelInventoryGUI {
    private static final int[] SLOTS = new int[]{
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
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    new GUIEmblem(emblem).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    String displayName = emblem.toString();
                    GUIMaterial guiMaterial = emblem.getGuiMaterial();
                    ArrayList<String> description = new ArrayList<>(emblem.getDescription());

                    ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                            "§8" + emblem.amountUnlocked(player) + " Unlocked",
                            " "
                    ));
                    lore.addAll(description);
                    lore.add(" ");
                    lore.add("§eClick to view!");

                    return ItemStackCreator.getUsingGUIMaterial("§a" + displayName, guiMaterial, 1, lore);
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
