package net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.emblem;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.swofty.types.generic.gui.inventory.ItemStackCreator;
import net.swofty.types.generic.gui.inventory.SkyBlockAbstractInventory;
import net.swofty.types.generic.gui.inventory.GUIItem;
import net.swofty.types.generic.gui.inventory.actions.SetTitleAction;
import net.swofty.types.generic.gui.inventory.inventories.sbmenu.levels.GUISkyBlockLevels;
import net.swofty.types.generic.levels.SkyBlockEmblems;
import net.swofty.types.generic.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class GUIEmblems extends SkyBlockAbstractInventory {
    private static final int[] SLOTS = new int[]{
            11, 12, 13, 14, 15
    };

    public GUIEmblems() {
        super(InventoryType.CHEST_4_ROW);
        doAction(new SetTitleAction(Component.text("Emblems")));
    }

    @Override
    public void handleOpen(SkyBlockPlayer player) {
        fill(ItemStackCreator.createNamedItemStack(Material.BLACK_STAINED_GLASS_PANE, "").build());

        setupCloseButton();
        setupBackButton();
        setupEmblemButtons(player);
    }

    private void setupCloseButton() {
        attachItem(GUIItem.builder(31)
                .item(ItemStackCreator.createNamedItemStack(Material.BARRIER, "§cClose").build())
                .onClick((ctx, item) -> {
                    ctx.player().closeInventory();
                    return true;
                })
                .build());
    }

    private void setupBackButton() {
        attachItem(GUIItem.builder(30)
                .item(ItemStackCreator.getStack("§aGo Back", Material.ARROW, 1,
                        "§7To SkyBlock Levels").build())
                .onClick((ctx, item) -> {
                    ctx.player().openInventory(new GUISkyBlockLevels());
                    return true;
                })
                .build());
    }

    private void setupEmblemButtons(SkyBlockPlayer player) {
        Arrays.stream(SkyBlockEmblems.values()).forEach(emblem -> {
            attachItem(GUIItem.builder(SLOTS[emblem.ordinal()])
                    .item(() -> {
                        String displayName = emblem.toString();
                        Material material = emblem.getDisplayMaterial();
                        ArrayList<String> description = new ArrayList<>(emblem.getDescription());

                        ArrayList<String> lore = new ArrayList<>(Arrays.asList(
                                "§8" + emblem.amountUnlocked(player) + " Unlocked",
                                " "
                        ));
                        lore.addAll(description);
                        lore.add(" ");
                        lore.add("§eClick to view!");

                        return ItemStackCreator.getStack("§a" + displayName, material, 1, lore).build();
                    })
                    .onClick((ctx, item) -> {
                        ctx.player().openInventory(new GUIInventoryEmblem(emblem));
                        return true;
                    })
                    .build());
        });
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