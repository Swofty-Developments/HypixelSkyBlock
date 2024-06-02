package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockInventoryGUI;
import net.swofty.types.generic.gui.inventory.item.GUIClickableItem;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIJamie extends SkyBlockInventoryGUI {
    public GUIJamie() {
        super("Claim Reward", InventoryType.CHEST_6_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, SkyBlockPlayer player) {
                player.addAndUpdateItem(ItemType.ROGUE_SWORD);
                player.closeInventory();
                player.sendMessage(Component.text("§aYou claimed a §fRogue Sword§a!"));
            }

            @Override
            public ItemStack.Builder getItem(SkyBlockPlayer player) {
                return ItemStackCreator.getStack("§fRogue Sword", Material.GOLDEN_SWORD, 1,
                        "§7Damage: §c+20",
                        "",
                        "§6Ability: Speed Boost §e§lRIGHT CLICK",
                        "§7Grants §f+100✦ Speed §7for",
                        "§a30 seconds§7.",
                        "§8Mana Cost: §350",
                        "",
                        "§8This item can be reforged!",
                        "§f§lCOMMON SWORD",
                        "",
                        "§eClick to claim!");
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
    }
}
