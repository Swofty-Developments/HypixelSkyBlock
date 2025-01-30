package net.swofty.types.generic.gui.inventory.inventories.auction;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.swofty.types.generic.data.DataHandler;
import net.swofty.types.generic.data.datapoints.DatapointAuctionEscrow;
import net.swofty.types.generic.gui.SkyBlockSignGUI;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.user.SkyBlockPlayer;

public class GUIAuctionDuration extends SkyBlockAbstractInventory {
    private boolean useMinutes = false;

    public GUIAuctionDuration() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Auction Duration")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        // Back button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To Create Auction").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIAuctionCreateItem(new GUIAuctionHouse()));
                    return true;
                })
                .build());

        // Duration buttons
        attachDurationButton(Material.RED_TERRACOTTA, 1, 10, player);
        attachDurationButton(Material.PINK_TERRACOTTA, 6, 11, player);
        attachDurationButton(Material.ORANGE_TERRACOTTA, 12, 12, player);
        attachDurationButton(Material.YELLOW_TERRACOTTA, 24, 13, player);
        attachDurationButton(Material.WHITE_TERRACOTTA, 48, 14, player);

        // Custom duration button
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§aCustom Duration", Material.COMPASS, 1,
                        "§7Specify how long you want",
                        "§7the auction to last.",
                        " ",
                        "§bRight-click for minutes!",
                        "§eLeft-click to set hours!").build())
                .onClick((ctx, item) -> {
                    useMinutes = ctx.clickType().equals(ClickType.RIGHT_CLICK);

                    SkyBlockSignGUI signGUI = new SkyBlockSignGUI(ctx.player());
                    signGUI.open(new String[]{"Enter duration", useMinutes ? "(in minutes)" : "(in hours)"}).thenAccept(lines -> {
                        handleCustomDuration(ctx.player(), lines);
                    });
                    return true;
                })
                .build());
    }

    private void attachDurationButton(Material color, int hours, int slot, SkyBlockPlayer player) {
        long millis = hours * 3600000L;
        DatapointAuctionEscrow.AuctionEscrow escrow = player.getDataHandler()
                .get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class).getValue();

        attachItem(GUIItem.builder(slot)
                .item(() -> {
                    ItemStack.Builder stack = ItemStackCreator.getStack("§a" + hours + " Hours", color, 1,
                            " ",
                            "§eClick to set this duration!");

                    if (escrow.getDuration() == millis) {
                        stack = ItemStackCreator.enchant(stack);
                    }

                    return stack.build();
                })
                .onClick((ctx, item) -> {
                    escrow.setDuration(millis);
                    ctx.player().openInventory(new GUIAuctionDuration());
                    return true;
                })
                .build());
    }

    private void handleCustomDuration(SkyBlockPlayer player, String input) {
        long duration;
        try {
            duration = Long.parseLong(input);
        } catch (NumberFormatException ex) {
            player.sendMessage("§cCould not read this number!");
            return;
        }

        if (duration <= 1) {
            player.sendMessage("§cInvalid time amount!");
            return;
        }

        if (duration >= 336 && !useMinutes) {
            player.sendMessage("§cYou can only put an auction up to a maximum of 14 days!");
            return;
        }

        player.getDataHandler().get(DataHandler.Data.AUCTION_ESCROW, DatapointAuctionEscrow.class)
                .getValue().setDuration(duration * (useMinutes ? 60000 : 3600000));

        player.openInventory(new GUIAuctionCreateItem(this));
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