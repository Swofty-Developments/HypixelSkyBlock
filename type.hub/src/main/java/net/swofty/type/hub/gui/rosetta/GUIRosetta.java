package net.swofty.type.hub.gui.rosetta;

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

public class GUIRosetta extends SkyBlockAbstractInventory {
    public GUIRosetta() {
        super(InventoryType.CHEST_6_ROW);
        doAction(new SetTitleAction(Component.text("Rosetta's Starter Gear")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE).build());

        // Close button
        attachItem(GUIItem.builder(49)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());

        // Iron Armor
        attachItem(GUIItem.builder(19)
                .item(ItemStackCreator.getStack("§eIron Armor", Material.IRON_HELMET, 1,
                        "§7Plain old iron armor.",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRosettaIronArmor());
                    return true;
                })
                .build());

        // Rosetta's Armor
        attachItem(GUIItem.builder(21)
                .item(ItemStackCreator.getStack("§eRosetta's Armor", Material.DIAMOND_HELMET, 1,
                        "§7Custom-designed and",
                        "§7hand-crafted diamond armor.",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIRosettaArmor());
                    return true;
                })
                .build());

        // Squire Armor
        attachItem(GUIItem.builder(14)
                .item(ItemStackCreator.getStack("§eSquire Armor", Material.CHAINMAIL_HELMET, 1,
                        "§7Solid set to venture into the",
                        "§7deep caverns.",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISquireArmor());
                    return true;
                })
                .build());

        // Mercenary Armor
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStack("§eMercenary Armor", Material.IRON_HELMET, 1,
                        "§7Kickstart your warrior",
                        "§7journey!",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIMercenaryArmor());
                    return true;
                })
                .build());

        // Celeste Armor
        attachItem(GUIItem.builder(32)
                .item(ItemStackCreator.getStack("§eCeleste Armor", Material.LEATHER_HELMET, 1,
                        "§7Dip a toe into the world of",
                        "§7magic.",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUICelesteArmor());
                    return true;
                })
                .build());

        // Starlight Armor
        attachItem(GUIItem.builder(34)
                .item(ItemStackCreator.getStack("§eStarlight Armor", Material.GOLDEN_HELMET, 1,
                        "§7This set was designed with the",
                        "§7help of Barry the Wizard.",
                        "",
                        "§eClick to view set!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIStarlightArmor());
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
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onSuddenQuit(SkyBlockPlayer player) {
    }
}