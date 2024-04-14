package net.swofty.types.generic.gui.inventory.inventories;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

public class GUIAnvil  extends SkyBlockInventoryGUI implements RefreshingGUI {

    public GUIAnvil() {
        super( "Anvil",
                InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");

        fill(ItemStackCreator.createNamedItemStack(Material.RED_STAINED_GLASS_PANE, ""), 45 , 53);

        set(GUIClickableItem.getCloseItem(49));
        set(new GUIItem(13) {
            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§cAnvil", Material.BARRIER, 1, "§7Place a target item in the left slot", "§7and a sacrifice item in the right slot", "§7to combine them!");
            }
        });

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                //TODO: Implement anvil combining
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aCombine Items", Material.ANVIL, 1, "§7Combine the items in the slots to the", "§7left and right below.");
            }
        });

        set(new GUIClickableItem(29) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                //TODO: Implement placing item to be combined
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.AIR);
            }
        });

        set(new GUIClickableItem(33) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                //TODO: Implement placing item to be combined
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.createNamedItemStack(Material.AIR);
            }
        });

        setValidItems();
    }

    private void setValidItems(){
        for (int i : new int[]{ 11,12,20 }){
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§6Item to Upgrade", Material.RED_STAINED_GLASS_PANE, 1, "§7The item you want to upgrade should", "§7be placed in the slot on this side.");
                }
            });
        }

        for (int i : new int[]{ 14,15,24 }){
            set(new GUIItem(i) {
                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return ItemStackCreator.getStack("§6Item to Sacrifice", Material.RED_STAINED_GLASS_PANE, 1, "§7The item you are sacrificing in order", "§7to upgrade the item on the left", "§7should be placed in the slot on this", "§7side.");
                }
            });
        }
    }

    @Override
    public void refreshItems(SkyBlockPlayer player) {

    }

    @Override
    public int refreshRate() {
        return 5;
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
