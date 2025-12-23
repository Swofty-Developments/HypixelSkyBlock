package net.swofty.type.goldmine.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.generic.gui.inventory.item.GUIItem;
import net.swofty.type.generic.user.HypixelPlayer;

public class GUIRusty extends HypixelInventoryGUI {
    public GUIRusty() {
        super("Rusty The Janitor", InventoryType.CHEST_4_ROW);
    }


    @Override
    public void onOpen(InventoryGUIOpenEvent event) {
        fill(ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE));

        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aWeapons", Material.IRON_SWORD, 1,
                        "§7Contains reobtainable weapons and",
                        "§7gear that you have lost from all",
                        "§7around SkyBlock.",
                        "§7",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aPets", Material.PLAYER_HEAD, 1,
                        "§7Contains pets, and some reobtainable",
                        "§7pet items from all around SkyBlock",
                        "§7",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aAccessories", Material.PLAYER_HEAD, 1,
                        "§7Contains accessories and talismans",
                        "§7from all around SkyBlock.",
                        "§7",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {

            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aMiscellaneous", Material.FILLED_MAP, 1,
                        "§7Contains tools, travel scrolls,",
                        "§7one-time qust rewards, and more",
                        "§7random junk.",
                        "§7",
                        "§eClick to view!"
                );
            }
        });

        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStackCreator.getStack(
                        "§aThe Janitor", Material.REDSTONE_TORCH, 1,
                        "§7Rusty watches over the neatness of the §6Gold",
                        "§6Mines§7, but really he watches over the whole of",
                        "§aSkyBlock§7.",
                        "§7",
                        "§7If you misplace a §6one-time-reward §7from a",
                        "§7quest, it may be offered here!"
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
