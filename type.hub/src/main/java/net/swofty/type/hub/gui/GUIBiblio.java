package net.swofty.type.hub.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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

public class GUIBiblio extends SkyBlockAbstractInventory {

    public GUIBiblio() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Skyblock Wiki")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Close button
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Wiki Command
        attachItem(GUIItem.builder(11)
                .item(ItemStackCreator.getStack("§dWiki Command", Material.PAINTING, 1,
                        "§7Visit the Wiki using §a/wiki §7and browse",
                        "§7the many pages and utilities.",
                        "",
                        "§7You can also specify an extra",
                        "§7argument when using §6/wiki <id> §7to",
                        "§7search via an item ID.",
                        "",
                        "§eClick to visit the Wiki!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage(Component.text("§7Click §e§lHERE §7to visit the §6Official SkyBlock Wiki§7!§r")
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://wiki.hypixel.net")));
                    return true;
                })
                .build());

        // Wiki Info
        attachItem(GUIItem.builder(13)
                .item(ItemStackCreator.getStack("§6The Skyblock Wiki", Material.WRITABLE_BOOK, 1,
                        "§7The newly finished §aOfficial SkyBlock",
                        "§aWiki §7has launched and contains lots",
                        "§7of useful information on items, mobs,",
                        "§7drop rates, areas, trivia, and more.",
                        "§7This is a §6Hypixel-led§7, §dcommunity",
                        "§dmaintained §7Wiki which aims to provide",
                        "§7the most accurate information in the",
                        "§7best way possible.",
                        "§8Edits",
                        " §6> 100,000+",
                        "",
                        "§8Pages",
                        " §d> 27,000+",
                        "",
                        "§8Files",
                        " §a> 15,000+",
                        "",
                        "§eSee more @ §fwiki.hypixel.net").build())
                .build());

        // Wikithis Command
        attachItem(GUIItem.builder(15)
                .item(ItemStackCreator.getStack("§aWikithis Command", Material.OAK_SIGN, 1,
                        "§7Want to view more information about",
                        "§7the item you are currently §dholding §7?",
                        "§7Then this is the command for §eyou§7!",
                        "",
                        "§7Running §6/wikithis §7whilst §aholding an",
                        "§aitem §7will attempt to find a Wiki page",
                        "§7for the item and then link you to it",
                        "§7in-game.",
                        "",
                        "§eClick to search your held item!").build())
                .onClick((ctx, item) -> {
                    ctx.player().sendMessage(Component.text("§cThis Feature is not there yet. §aOpen a Pull request HERE to get it added quickly!")
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Swofty-Developments/HypixelSkyBlock")));
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