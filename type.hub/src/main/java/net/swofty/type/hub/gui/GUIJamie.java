package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIJamie extends SkyBlockAbstractInventory {
    private static final String STATE_UNCLAIMED = "unclaimed";
    private static final String STATE_CLAIMED = "claimed";

    public GUIJamie() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Claim Reward")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Reward item
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§fRogue Sword", Material.GOLDEN_SWORD, 1,
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
                        "§eClick to claim!").build())
                .onClick((ctx, item) -> {
                    player.addAndUpdateItem(ItemType.ROGUE_SWORD);
                    player.closeInventory();
                    player.sendMessage(Component.text("§aYou claimed a §fRogue Sword§a!"));
                    return true;
                })
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No special cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No special cleanup needed
    }
}