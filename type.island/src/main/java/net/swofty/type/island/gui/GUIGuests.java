package net.swofty.type.island.gui;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIGuests extends SkyBlockAbstractInventory {
    public GUIGuests() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Jerry's Guide to Guesting")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Jerry the Assistant").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIJerry());
                    return true;
                })
                .build());

        // Visit islands button
        attachItem(GUIItem.builder(10)
                .item(ItemStackCreator.getStack("§aVisit player islands", Material.FEATHER, 1,
                        "§7You can get Guest on other islands",
                        "§7using §a/visit <player>",
                        "",
                        "§7Guests §cCan't interact with the",
                        "§7world, but it's always fun to see",
                        "§7what others are up to!").build())
                .build());

        // Guest limits button
        attachItem(GUIItem.builder(12)
                .item(ItemStackCreator.getStack("§aGuests limit", Material.SHORT_GRASS, 1,
                        "§7You can only host a limited",
                        "§7number of §aguests §7on your",
                        "§7island concurrently.",
                        "",
                        "§7The limit depends on your rank:",
                        "§7- §c[§fYOUTUBE§c] §f= §a15",
                        "§7- §6[MVP§c++§6] §f= §a7",
                        "§7- §b[MVP] §f= §a5",
                        "§7- §a[VIP] §f= §a3",
                        "§7- Default §f= §a1",
                        "",
                        "§7Limit on the island: §a1 guests",
                        "",
                        "§bCo-op profile use the partner",
                        "§bwith the highest rank!",
                        "",
                        "§epurchase rank at store.hypixel.net!").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    ctx.player().sendMessage("§eVisit our web store: §6https://store.hypixel.net");
                    return true;
                })
                .build());

        // Access permissions button
        attachItem(GUIItem.builder(14)
                .item(ItemStackCreator.getStack("§aAccess Permissions", Material.OAK_FENCE, 1,
                        "§7You may edit who is able to",
                        "§7guest on your island in your",
                        "§eIsland Settings§7.",
                        "",
                        "§7Use §c/ignore add <username> to",
                        "§7prevent a specific player from",
                        "§7joining.",
                        "",
                        "§eClick to open island settings!").build())
                .build());

        // Moderation button
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§aModeration", Material.REPEATER, 1,
                        "§7Manage online guests using the",
                        "§eGuests Management §7menu.",
                        "",
                        "§7Alternatively, use the §c/sbkick &",
                        "§a/sbkickall commands or §aclicking",
                        "§aon them§7.").build())
                .build());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent event, CloseReason reason) {
        // No cleanup needed
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
        // No cleanup needed
    }
}