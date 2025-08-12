package net.swofty.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import lombok.SneakyThrows;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.type.generic.gui.inventory.HypixelInventoryGUI;
import net.swofty.type.generic.gui.inventory.ItemStackCreator;
import net.swofty.type.generic.gui.inventory.item.GUIClickableItem;
import SkyBlockPlayer;

public class GUIProfileSelectMode extends HypixelInventoryGUI {
    public GUIProfileSelectMode() {
        super("Choose a SkyBlock Mode", InventoryType.CHEST_4_ROW);
    }

    @SneakyThrows
    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getGoBackItem(31, new GUIProfileManagement()));

        set(new GUIClickableItem(11) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§aClassic Profile", Material.GRASS_BLOCK, 1,
                        "§8SkyBlock Mode",
                        "",
                        "§7A SkyBlock adventure with the",
                        "§7default rules.",
                        "",
                        "§7Start on a new tiny island,",
                        "§7without gear and build your",
                        "§7way up to become the",
                        "§agreatest player§7 in the",
                        "§7universe!",
                        "",
                        "§eClick to play this mode!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                new GUIProfileCreate().open(player);
            }
        });

        set(new GUIClickableItem(15) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                return ItemStackCreator.getStack("§6Special Modes", Material.BLAZE_POWDER, 1,
                        "§7Choose a SkyBlock mode with",
                        "§7special rules and unique",
                        "§7mechanics.",
                        "",
                        "§eClick to choose a mode!"
                );
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p; 
                player.sendMessage("§cSpecial Modes in SkyBlock are currently unavailable. Please check back another time.");
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
    public void suddenlyQuit(Inventory inventory, SkyBlockPlayer player) {

    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
