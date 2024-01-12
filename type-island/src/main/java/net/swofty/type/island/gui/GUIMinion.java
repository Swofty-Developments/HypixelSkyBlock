package net.swofty.type.island.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.RefreshingGUI;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.gui.inventory.item.GUIItem;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.updater.NonPlayerItemUpdater;
import net.swofty.types.generic.minion.IslandMinionData;
import net.swofty.types.generic.minion.SkyBlockMinion;
import net.swofty.types.generic.user.SkyBlockPlayer;
import net.swofty.types.generic.utility.StringUtility;

import java.util.Arrays;

public class GUIMinion extends SkyBlockInventoryGUI implements RefreshingGUI {
    private static final int[] SLOTS = new int[]{
            21, 22, 23, 24, 25,
            30, 31, 32, 33, 34,
            39, 40, 41, 42, 43
    };
    private final IslandMinionData.IslandMinion minion;

    public GUIMinion(IslandMinionData.IslandMinion minion) {
        super(minion.getMinion().getDisplay() + " " + StringUtility.getAsRomanNumeral(minion.getTier()),
                InventoryType.CHEST_6_ROW);

        this.minion = minion;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(Material.BLACK_STAINED_GLASS_PANE, "");

        set(new GUIClickableItem() {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {

            }

            @Override
            public int getSlot() {
                return 3;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§aIdeal Layout", Material.REDSTONE_TORCH, 1,
                        "§7View the most efficient spot for this",
                        "§7minion to be placed in.",
                        " ",
                        "§eClick to view!");
            }
        });

        set(new GUIItem() {
            @Override
            public int getSlot() {
                return 4;
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return new NonPlayerItemUpdater(new SkyBlockItem(minion.getMinion().getItem())).getUpdatedItem();
            }
        });

        SkyBlockMinion.MinionTier minionTier = minion.getMinion().asSkyBlockMinion().getTiers().get(
                minion.getTier() - 1
        );

        int i = 0;
        for (int slot : SLOTS) {
            i++;
            boolean unlocked = minionTier.getSlots() >= i;

            set(new GUIItem() {
                @Override
                public int getSlot() {
                    return slot;
                }

                @Override
                public ItemStack.Builder getItem(SkyBlockPlayer player) {
                    return unlocked ? ItemStack.builder(Material.AIR) :
                            ItemStackCreator.createNamedItemStack(Material.WHITE_STAINED_GLASS_PANE);
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

    }
}
