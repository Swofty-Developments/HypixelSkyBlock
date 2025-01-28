package net.swofty.types.generic.gui.inventory.inventories;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIBoosterCookie extends SkyBlockAbstractInventory {

    public GUIBoosterCookie() {
        super(InventoryType.CHEST_3_ROW);
        doAction(new SetTitleAction(Component.text("Consume Booster Cookie?")));
    }

    @Override
    protected void handleOpen(SkyBlockPlayer player2) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Cancel button
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§cCancel", Material.RED_CONCRETE, 1,
                        "§7I'm not hungry...").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Consume cookie button
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§eConsume Cookie", Material.COOKIE, 1,
                        "§7Gain the §dCookie Buff§!",
                        " ",
                        "§7Duration: §b4 days§!",
                        " ",
                        "§7You will be able to gain",
                        "§b4,000 Bits §7from this",
                        "§7cookie.").build())
                .onClick((ctx, item) -> {
                    SkyBlockPlayer player = ctx.player();
                    long time = 0;
                    if (player.getBoosterCookieExpirationDate() - System.currentTimeMillis() > System.currentTimeMillis()) {
                        time = player.getBoosterCookieExpirationDate() - System.currentTimeMillis() + System.currentTimeMillis();
                    } else {
                        time = System.currentTimeMillis();
                    }
                    time += 345600000; // 4 days
                    player.setBoosterCookieExpirationDate(time);
                    player.setBits(player.getBits() + 4000);
                    player.setItemInMainHand(ItemStack.AIR);
                    player.closeInventory();
                    return true;
                })
                .build());
    }

    @Override
    protected boolean allowHotkeying() {
        return false;
    }

    @Override
    protected void onClose(InventoryCloseEvent event, CloseReason reason) {
    }

    @Override
    protected void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    protected void onSuddenQuit(SkyBlockPlayer player) {
    }
}