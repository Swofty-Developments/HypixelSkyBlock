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
import net.swofty.types.generic.gui.inventory.actions.AddStateAction;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUILonelyPhilosopher extends SkyBlockAbstractInventory {
    private static final String STATE_CAN_AFFORD = "can_afford";
    private static final String STATE_CANNOT_AFFORD = "cannot_afford";
    private static final int SCROLL_COST = 150000;

    public GUILonelyPhilosopher() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Lonely Philosopher")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Set initial state based on player's coins
        if (player.getCoins() >= SCROLL_COST) {
            doAction(new AddStateAction(STATE_CAN_AFFORD));
        } else {
            doAction(new AddStateAction(STATE_CANNOT_AFFORD));
        }

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Travel Scroll
        attachItem(GUIItem.builder(22)
                .item(ItemStackCreator.getStack("§5Travel Scroll To Hub Castle", Material.MAP, 1,
                        "§7Consume this item to add its",
                        "§7destination to your fast travel",
                        "§7options.",
                        "",
                        "§7Requires §bMVP§c+ §7to consume!",
                        "",
                        "§7Island: §aHub",
                        "§7Teleport: §eCastle",
                        "",
                        "§5§lEPIC TRAVEL SCROLL",
                        "",
                        "§7Cost",
                        "§6150,000 Coins").build())
                .onClick((ctx, item) -> {
                    double coins = player.getCoins();

                    if (coins < SCROLL_COST) {
                        player.sendMessage("§cYou don't have enough coins!");
                        return false;
                    }

                    player.addAndUpdateItem(ItemType.HUB_CASTLE_TRAVEL_SCROLL);
                    player.playSuccessSound();
                    player.setCoins(coins - SCROLL_COST);
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