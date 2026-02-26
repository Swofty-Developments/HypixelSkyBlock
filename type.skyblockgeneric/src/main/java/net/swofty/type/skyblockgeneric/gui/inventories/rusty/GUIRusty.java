package net.swofty.type.skyblockgeneric.gui.inventories.rusty;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.i18n.I18n;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIRusty extends HypixelInventoryGUI {
    public GUIRusty() {
        super(I18n.string("gui_rusty.main.title"), InventoryType.CHEST_4_ROW);
    }


    @Override
    public void onOpen(InventoryGUIOpenEvent event) {
        fill(ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE));

        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIRustyWeaponsAndGear().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        I18n.string("gui_rusty.main.weapons_button"), Material.IRON_SWORD, 1,
                        I18n.lore("gui_rusty.main.weapons_button.lore")
                );
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIRustyPetsAndPetItems().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        I18n.string("gui_rusty.main.pets_button"), "4e794274c1bb197ad306540286a7aa952974f5661bccf2b725424f6ed79c7884", 1,
                        I18n.lore("gui_rusty.main.pets_button.lore")
                );
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIRustyAccessories().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStackHead(
                        I18n.string("gui_rusty.main.accessories_button"), "3ada666715bfd2aa9fbd81daef59b9fe1c96c4fa0d08dbc72eae5633177dbf88", 1,
                        I18n.lore("gui_rusty.main.accessories_button.lore")
                );
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIRustyMiscellaneous().open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        I18n.string("gui_rusty.main.miscellaneous_button"), Material.FILLED_MAP, 1,
                        I18n.lore("gui_rusty.main.miscellaneous_button.lore")
                );
            }
        });

        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        I18n.string("gui_rusty.main.janitor_info"), Material.REDSTONE_TORCH, 1,
                        I18n.lore("gui_rusty.main.janitor_info.lore")
                );
            }
        });

        set(GUIClickableItem.getCloseItem(31));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }
}
