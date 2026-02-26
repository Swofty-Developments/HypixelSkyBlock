package net.swofty.type.skyblockgeneric.gui.inventories.builder;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.swofty.type.generic.user.HypixelPlayer;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIBuilder extends HypixelInventoryGUI {
    public GUIBuilder() {
        super("Builder", InventoryType.CHEST_4_ROW);
    }
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(new GUIClickableItem(10) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIShopBuilderWoodworking());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aWoodworking", Material.OAK_PLANKS, 1, "§7Wood-related blocks!");
            }
        });
        set(new GUIClickableItem(12) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIShopBuilderRocksBricks());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aRocks & Bricks", Material.STONE, 1,
                        "§7Rocks, stones, sands and brick",
                        "§7blocks.");
            }
        });
        set(new GUIClickableItem(14) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIShopBuilderGreenThumb());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStack("§aGreen Thumb", Material.ROSE_BUSH, 1,
                        "§7Everything you need to grow a",
                        "§7nice garden.");
            }
        });
        set(new GUIClickableItem(16) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.openView(new GUIShopBuilderVariety());
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                return ItemStackCreator.getStackHead("§aVariety", "3c2d8e8ec2737b599a48fc07ea58b806969e6021802019992dda32a653794df6", 1,
                        "§7Weird blocks and an assortment",
                        "§7of decorative fruits.");
            }
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
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
