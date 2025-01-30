package net.swofty.type.hub.gui.builder;

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

public class GUIBuilder extends SkyBlockAbstractInventory {
    public GUIBuilder() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Builder")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, " ").build());

        // Woodworking
        attachItem(GUIItem.builder(10)
                .item(ItemStackCreator.getStack("§aWoodworking", Material.OAK_PLANKS, 1,
                        "§7Wood-related blocks!").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIShopBuilderWoodworking());
                    return true;
                })
                .build());

        // Rocks & Bricks
        attachItem(GUIItem.builder(12)
                .item(ItemStackCreator.getStack("§aRocks & Bricks", Material.STONE, 1,
                        "§7Rocks, stones, sands and brick",
                        "§7blocks.").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIShopBuilderRocksBricks());
                    return true;
                })
                .build());

        // Green Thumb
        attachItem(GUIItem.builder(14)
                .item(ItemStackCreator.getStack("§aGreen Thumb", Material.ROSE_BUSH, 1,
                        "§7Everything you need to grow a",
                        "§7nice garden.").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIShopBuilderGreenThumb());
                    return true;
                })
                .build());

        // Variety
        attachItem(GUIItem.builder(16)
                .item(ItemStackCreator.getStackHead("§aVariety",
                        "3c2d8e8ec2737b599a48fc07ea58b806969e6021802019992dda32a653794df6", 1,
                        "§7Weird blocks and an assortment",
                        "§7of decorative fruits.").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUIShopBuilderVariety());
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